<template>
  <div class="chart-container">
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import {
  Chart,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  LineController,
  BarController,
  Title,
  Tooltip,
  Legend,
  Filler
} from 'chart.js'
import type { ChartData } from '@/types'

Chart.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  LineController,
  BarController,
  Title,
  Tooltip,
  Legend,
  Filler
)

interface Props {
  chartData: ChartData
  type?: 'line' | 'bar'
  color?: string
}

const props = withDefaults(defineProps<Props>(), {
  type: 'line',
  color: '#3b82f6'
})

const chartCanvas = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart | null = null

const createChart = () => {
  if (!chartCanvas.value) return

  const labels = props.chartData.data.map((d) =>
    new Date(d.date).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short' })
  )
  const values = props.chartData.data.map((d) => d.value)

  const config = {
    type: props.type,
    data: {
      labels,
      datasets: [
        {
          label: props.chartData.label,
          data: values,
          borderColor: props.color,
          backgroundColor: props.type === 'line' ? `${props.color}33` : props.color,
          tension: 0.4,
          fill: props.type === 'line'
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          mode: 'index' as const,
          intersect: false
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: function (value: string | number) {
              if (typeof value === 'number') {
                return value.toLocaleString('fr-FR')
              }
              return value
            }
          }
        }
      }
    }
  }

  chartInstance = new Chart(chartCanvas.value, config)
}

const updateChart = () => {
  if (chartInstance) {
    chartInstance.destroy()
  }
  createChart()
}

onMounted(() => {
  createChart()
})

watch(
  () => props.chartData,
  () => {
    updateChart()
  },
  { deep: true }
)
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 300px;
  width: 100%;
}
</style>
