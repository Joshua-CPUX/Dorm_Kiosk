<template>
  <div class="order-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单列表</span>
        </div>
      </template>

      <div class="search-bar">
        <el-select v-model="searchStatus" placeholder="订单状态" style="width: 150px; margin-right: 10px;" clearable>
          <el-option label="待支付" :value="1" />
          <el-option label="已支付" :value="2" />
          <el-option label="配送中" :value="3" />
          <el-option label="已完成" :value="4" />
          <el-option label="已取消" :value="5" />
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>

      <el-table :data="orderList" style="width: 100%; margin-top: 20px;" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column label="订单类型" width="100">
          <template #default="{ row }">
            {{ row.orderType === 1 ? '自取' : '配送' }}
          </template>
        </el-table-column>
        <el-table-column label="订单金额" width="120">
          <template #default="{ row }">
            ¥{{ row.payAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ statusMap[row.status] || row.statusName || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button
              v-if="row.status === 2"
              type="success"
              size="small"
              @click="handleUpdateStatus(row, 3)"
            >
              发货
            </el-button>
            <el-button
              v-if="row.status === 3"
              type="warning"
              size="small"
              @click="handleUpdateStatus(row, 4)"
            >
              完成
            </el-button>
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

    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <el-descriptions :column="2" border v-if="currentOrder">
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单类型">
          {{ currentOrder.orderType === 1 ? '自取' : '配送' }}
        </el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.status)">
            {{ statusMap[currentOrder.status] || currentOrder.statusName || '-' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
        <el-descriptions-item label="商品总额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="实付金额">
          <span style="color: #F56C6C; font-weight: bold;">¥{{ currentOrder.payAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">
          <template v-if="currentOrder.address">
            {{ currentOrder.address.consignee }} {{ currentOrder.address.phone }}
            {{ currentOrder.address.fullAddress }}
          </template>
          <template v-else>-</template>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          {{ currentOrder.remark || '-' }}
        </el-descriptions-item>
      </el-descriptions>

      <el-divider>商品明细</el-divider>

      <el-table :data="currentOrder?.items || []" style="width: 100%;">
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="price" label="单价" width="100">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100" align="center" />
        <el-table-column prop="totalPrice" label="小计" width="100">
          <template #default="{ row }">¥{{ row.totalPrice }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getOrderList, getOrderDetail, updateOrderStatus } from '../../api/admin';

const orderList = ref([]);
const loading = ref(false);
const searchStatus = ref(null);
const detailVisible = ref(false);
const currentOrder = ref(null);

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
});

const statusMap = { 1: '待支付', 2: '已支付', 3: '配送中', 4: '已完成', 5: '已取消' };

const getStatusType = (status) => {
  const types = { 1: 'warning', 2: '', 3: 'primary', 4: 'success', 5: 'info' };
  return types[status] || '';
};

const loadData = async () => {
  loading.value = true;
  try {
    const res = await getOrderList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      status: searchStatus.value
    });
    orderList.value = res.data?.records || [];
    pagination.total = res.data?.total || 0;
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const handleDetail = async (row) => {
  try {
    const res = await getOrderDetail(row.id);
    currentOrder.value = res.data;
    detailVisible.value = true;
  } catch (error) {
    console.error(error);
  }
};

const handleUpdateStatus = async (row, status) => {
  const statusNames = { 3: '发货', 4: '完成' };
  try {
    await updateOrderStatus(row.id, status);
    ElMessage.success(`订单已${statusNames[status]}`);
    loadData();
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.order-container {
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
