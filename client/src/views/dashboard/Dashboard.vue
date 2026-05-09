<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #409EFF;">
            <el-icon><Shop /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ dashboardData.todayOrders || 0 }}</div>
            <div class="stat-label">今日订单</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #67C23A;">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ dashboardData.todaySales || 0 }}</div>
            <div class="stat-label">今日销售额</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #E6A23C;">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ dashboardData.pendingOrders || 0 }}</div>
            <div class="stat-label">待处理订单</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #F56C6C;">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ dashboardData.totalUsers || 0 }}</div>
            <div class="stat-label">总用户数</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <span>销售趋势</span>
          </template>
          <div ref="trendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <span>订单状态分布</span>
          </template>
          <div ref="pieChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>热销商品 TOP 5</span>
          </template>
          <el-table :data="rankingData" style="width: 100%">
            <el-table-column prop="productName" label="商品名称" />
            <el-table-column prop="sales" label="销量" width="100" align="center" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>销售统计</span>
          </template>
          <div class="sales-stats">
            <div class="sales-stat-item">
              <span class="label">本周销售额</span>
              <span class="value">¥{{ dashboardData.weekSales || 0 }}</span>
            </div>
            <div class="sales-stat-item">
              <span class="label">本月销售额</span>
              <span class="value">¥{{ dashboardData.monthSales || 0 }}</span>
            </div>
            <div class="sales-stat-item">
              <span class="label">总销售额</span>
              <span class="value">¥{{ dashboardData.totalSales || 0 }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue';
import * as echarts from 'echarts';
import { getDashboardData, getSalesTrend, getProductRanking, getOrderStatusStats } from '../../api/admin';

const dashboardData = ref({});
const trendData = ref([]);
const rankingData = ref([]);
const orderStats = ref({});
const trendChartRef = ref(null);
const pieChartRef = ref(null);

let trendChart = null;
let pieChart = null;

const loadData = async () => {
  try {
    const [dashboardRes, trendRes, rankingRes, statsRes] = await Promise.all([
      getDashboardData(),
      getSalesTrend(7),
      getProductRanking(5),
      getOrderStatusStats()
    ]);

    dashboardData.value = dashboardRes.data;
    trendData.value = trendRes.data || [];
    rankingData.value = rankingRes.data || [];
    orderStats.value = statsRes.data || {};

    await nextTick();
    initCharts();
  } catch (error) {
    console.error(error);
  }
};

const initCharts = () => {
  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value);
    const dates = trendData.value.map(item => item.date);
    const sales = trendData.value.map(item => item.sales);

    trendChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: dates },
      yAxis: { type: 'value' },
      series: [{
        data: sales,
        type: 'line',
        smooth: true,
        areaStyle: { color: 'rgba(64, 158, 255, 0.2)' },
        lineStyle: { color: '#409EFF' },
        itemStyle: { color: '#409EFF' }
      }]
    });
  }

  if (pieChartRef.value) {
    pieChart = echarts.init(pieChartRef.value);
    const statusNames = ['待支付', '已支付', '配送中', '已完成', '已取消'];
    const statusData = [1, 2, 3, 4, 5].map(status => ({
      name: statusNames[status - 1],
      value: orderStats.value[`status_${status}`] || 0
    }));

    pieChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: 0 },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: { show: false },
        emphasis: {
          label: { show: true, fontSize: 14 }
        },
        data: statusData,
        color: ['#909399', '#409EFF', '#E6A23C', '#67C23A', '#F56C6C']
      }]
    });
  }
};

onMounted(() => {
  loadData();
  window.addEventListener('resize', () => {
    trendChart?.resize();
    pieChart?.resize();
  });
});
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.stat-icon .el-icon {
  font-size: 28px;
  color: #fff;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 8px;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.sales-stats {
  padding: 20px;
}

.sales-stat-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.sales-stat-item:last-child {
  border-bottom: none;
}

.sales-stat-item .label {
  color: #666;
}

.sales-stat-item .value {
  font-size: 18px;
  font-weight: 600;
  color: #409EFF;
}
</style>
