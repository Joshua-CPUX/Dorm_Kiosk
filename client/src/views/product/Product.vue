<template>
  <div class="product-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品列表</span>
          <el-button type="primary" @click="handleAdd">添加商品</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商品名称"
          style="width: 200px; margin-right: 10px;"
          @keyup.enter="loadData"
        />
        <el-select v-model="searchCategory" placeholder="选择分类" style="width: 150px; margin-right: 10px;" clearable>
          <el-option
            v-for="cat in categoryList"
            :key="cat.id"
            :label="cat.name"
            :value="cat.id"
          />
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>

      <el-table :data="productList" style="width: 100%; margin-top: 20px;" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="商品图片" width="100">
          <template #default="{ row }">
            <el-image
              v-if="row.image"
              :src="row.image"
              style="width: 60px; height: 60px;"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="categoryId" label="分类" width="100">
          <template #default="{ row }">
            {{ getCategoryName(row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="sales" label="销量" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              :type="row.status === 1 ? 'warning' : 'success'"
              size="small"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end;"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="productForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productForm.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品分类" prop="categoryId">
          <el-select v-model="productForm.categoryId" placeholder="请选择分类" style="width: 100%;">
            <el-option
              v-for="cat in categoryList"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="副标题" prop="subtitle">
          <el-input v-model="productForm.subtitle" placeholder="请输入副标题" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="productForm.price" :min="0" :precision="2" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="原价">
          <el-input-number v-model="productForm.originalPrice" :min="0" :precision="2" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="productForm.stock" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-input v-model="productForm.image" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="productForm.description" type="textarea" :rows="3" placeholder="请输入商品描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  getProductList,
  addProduct,
  updateProduct,
  deleteProduct,
  updateProductStatus,
  getCategoryList
} from '../../api/admin';

const productList = ref([]);
const categoryList = ref([]);
const loading = ref(false);
const searchKeyword = ref('');
const searchCategory = ref(null);

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
});
const dialogVisible = ref(false);
const dialogTitle = ref('添加商品');
const isEdit = ref(false);
const formRef = ref(null);

const productForm = reactive({
  id: null,
  name: '',
  categoryId: null,
  subtitle: '',
  price: 0,
  originalPrice: 0,
  stock: 0,
  image: '',
  description: ''
});

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
};

const loadData = async () => {
  loading.value = true;
  try {
    const [productRes, categoryRes] = await Promise.all([
      getProductList({
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize,
        keyword: searchKeyword.value || undefined,
        categoryId: searchCategory.value || undefined
      }),
      getCategoryList()
    ]);
    productList.value = productRes.data?.records || productRes.data || [];
    pagination.total = productRes.data?.total || 0;
    categoryList.value = categoryRes.data || [];
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const getCategoryName = (categoryId) => {
  const category = categoryList.value.find(c => c.id === categoryId);
  return category ? category.name : '-';
};

const handleAdd = () => {
  dialogTitle.value = '添加商品';
  isEdit.value = false;
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  dialogTitle.value = '编辑商品';
  isEdit.value = true;
  Object.assign(productForm, {
    id: row.id,
    name: row.name,
    categoryId: row.categoryId,
    subtitle: row.subtitle || '',
    price: row.price,
    originalPrice: row.originalPrice,
    stock: row.stock,
    image: row.image || '',
    description: row.description || ''
  });
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateProduct(productForm);
          ElMessage.success('更新成功');
        } else {
          await addProduct(productForm);
          ElMessage.success('添加成功');
        }
        dialogVisible.value = false;
        loadData();
      } catch (error) {
        console.error(error);
      }
    }
  });
};

const handleToggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1;
  try {
    await updateProductStatus(row.id, newStatus);
    ElMessage.success(newStatus === 1 ? '上架成功' : '下架成功');
    loadData();
  } catch (error) {
    console.error(error);
  }
};

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
      type: 'warning'
    });
    await deleteProduct(row.id);
    ElMessage.success('删除成功');
    loadData();
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error);
    }
  }
};

const resetForm = () => {
  Object.assign(productForm, {
    id: null,
    name: '',
    categoryId: null,
    subtitle: '',
    price: 0,
    originalPrice: 0,
    stock: 0,
    image: '',
    description: ''
  });
  formRef.value?.resetFields();
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.product-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  display: flex;
  align-items: center;
}
</style>
