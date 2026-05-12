<template>
  <div class="category-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类列表</span>
          <el-button type="primary" @click="handleAdd">添加分类</el-button>
        </div>
      </template>

      <el-table :data="categoryList" style="width: 100%;" v-loading="loading">
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="icon" label="图标" width="120">
          <template #default="{ row }">
            <template v-if="row.icon">
              <!-- 如果是URL路径，显示图片 -->
              <el-image
                v-if="row.icon.indexOf('http') === 0 || row.icon.indexOf('/') === 0"
                :src="getImageUrl(row.icon)"
                fit="cover"
                style="width: 50px; height: 50px; border-radius: 8px;"
              />
              <!-- 否则显示emoji -->
              <span v-else style="font-size: 32px;">{{ row.icon }}</span>
            </template>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form :model="categoryForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类图标">
          <div class="upload-container">
            <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :before-upload="beforeUpload"
              :http-request="customUpload"
              :limit="1"
            >
              <el-image v-if="categoryForm.icon && (categoryForm.icon.indexOf('http') === 0 || categoryForm.icon.indexOf('/') === 0)" :src="getImageUrl(categoryForm.icon)" class="avatar" fit="cover" />
              <span v-else-if="categoryForm.icon" class="emoji-icon">{{ categoryForm.icon }}</span>
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
            <div class="upload-tip">
              <div>点击上传图片</div>
              <div style="margin-top: 8px;">
                <el-input
                  v-model="categoryForm.icon"
                  placeholder="或输入emoji/URL"
                  style="width: 200px;"
                />
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="categoryForm.sort" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="categoryForm.status"
            :active-value="1"
            :inactive-value="0"
            active-text="正常"
            inactive-text="禁用"
          />
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
import { Plus } from '@element-plus/icons-vue';
import { getCategoryList, addCategory, updateCategory, deleteCategory, uploadFile } from '../../api/admin';
import { getImageUrl } from '../../utils/image';

const categoryList = ref([]);
const loading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref('添加分类');
const isEdit = ref(false);
const formRef = ref(null);

const categoryForm = reactive({
  id: null,
  name: '',
  icon: '',
  sort: 0,
  status: 1
});

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
};

const loadData = async () => {
  loading.value = true;
  try {
    const res = await getCategoryList();
    categoryList.value = res.data || [];
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const handleAdd = () => {
  dialogTitle.value = '添加分类';
  isEdit.value = false;
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  dialogTitle.value = '编辑分类';
  isEdit.value = true;
  Object.assign(categoryForm, {
    id: row.id,
    name: row.name,
    icon: row.icon || '',
    sort: row.sort,
    status: row.status ?? 1
  });
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateCategory(categoryForm);
          ElMessage.success('更新成功');
        } else {
          await addCategory(categoryForm);
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

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
      type: 'warning'
    });
    await deleteCategory(row.id);
    ElMessage.success('删除成功');
    loadData();
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error);
    }
  }
};

const resetForm = () => {
  Object.assign(categoryForm, {
    id: null,
    name: '',
    icon: '',
    sort: 0,
    status: 1
  });
  formRef.value?.resetFields();
};

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  const isLt10M = file.size / 1024 / 1024 < 10;
  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return false;
  }
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB!');
    return false;
  }
  return true;
};

const customUpload = async (options) => {
  const { file, onSuccess, onError } = options;
  try {
    const res = await uploadFile(file);
    if (res.code === 200 || res.code === 0) {
      categoryForm.icon = res.data?.url || res.data;
      ElMessage.success('上传成功');
      onSuccess?.();
    } else {
      ElMessage.error(res.msg || '上传失败');
      onError?.(new Error(res.msg || '上传失败'));
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('上传失败');
    onError?.(error);
  }
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.category-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-container {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.avatar-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}

.emoji-icon {
  font-size: 48px;
}

.upload-tip {
  color: #909399;
  font-size: 12px;
}
</style>
