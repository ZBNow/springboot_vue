<template>
  <div style="width: 99%">
    <div class="card" v-if="data.user.role === 'USER'">欢迎来到大郑王朝</div>
    <div v-if="data.user.role ==='USER'" class="card" style="margin-top: 20px; width:50%;" >
      <div style="margin-bottom: 20px; font-size: 20px">系统公告</div>
      <el-timeline>
        <el-timeline-item :timestamp="item.time" placement="top" color="#0bbd87" v-for="item in data.noticeTable">
          <h3>{{ item.title}}</h3>
          <p>{{ item.content}}</p>
        </el-timeline-item>
      </el-timeline>
    </div>

    <div v-else style="">
      <div style=" display: flex; grid-gap: 10px">
        <div class="card" style="height: 400px; width: 50%;" id="pie"></div>
        <div class="card" style="height: 400px; width: 50%;" id="bar"></div>
      </div>

      <div class="card" style="height: 400px; width: 100%; margin-top: 10px" id="line"></div>
    </div>
  </div>

</template>

<script setup>
import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from 'echarts';

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  noticeTable: [],
})
console.log(data.user)
const load = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeTable = res.data;
      if (data.noticeTable.length > 3){
        data.noticeTable = data.noticeTable.slice(0, 3)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const loadBar = () => {
  request.get('/echarts/bar').then(res => {
    if(res.code === '200'){
      let charDom = document.getElementById('bar')
      let myChart = echarts.init(charDom)
      barOptions.xAxis.data = res.data.xAxis
      barOptions.series[0].data = res.data.yAxis
      myChart.setOption(barOptions)
    }
  })
}
const loadLine = () => {
  request.get('/echarts/line').then(res => {
    if(res.code === '200'){
      let charDom = document.getElementById('line')
      let myChart = echarts.init(charDom)
      lineOptions.xAxis.data = res.data.xAxis
      lineOptions.series[0].data = res.data.yAxis
      myChart.setOption(lineOptions)
    }
  })
}
const loadPie = () => {

  request.get("/echarts/pie").then(res => {
    if(res.code === '200'){
      let charDom = document.getElementById('pie')
      let myChart = echarts.init(charDom)
      pieOption.series[0].data = res.data
      myChart.setOption(pieOption)
    }
  })

}
// onMounted()等页面元素渲染之后，在执行,否则会无法找到pie的id
onMounted(() => {
  loadPie()
  loadBar()
  loadLine()
})

//折线图
let lineOptions = {
  title: {
    text: '最近一周每天平台帖子发布数量',
    subtext: '',
    left: 'center'
  },
  legend: {
    data: [],
    template:""

  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  tooltip: {
    trigger: 'item' // 提示框触发方式
  },
  xAxis: {
    name: '日期', // 横轴名称
    type: 'category', // 横轴类型为类目轴
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'] // 横轴数据
  },
  yAxis: {
    name: '攻略数量', // 纵轴名称
    type: 'value' // 纵轴类型为数值轴
  },
  series: [
    {
      name: '攻略数量', // 系列名称
      data: [820, 932, 901, 934, 1290, 1330, 1320], // 系列数据
      type: 'line', // 图表类型为折线图
      smooth: true, // 是否平滑显示
      markLine: {
        data: [{type: 'average', name: '最近一周场攻略发布数量平均值'}] // 标记线，显示平均值
      },
      markPoint: {
        data: [
          {type: 'max', name: '最大值'}, // 标记点，显示最大值
          {type: 'min', name: '最小值'} // 标记点，显示最小值
        ],
      },
    },
  ]
}

// 柱状图
let barOptions = {
  title: {
    text: '不同类型旅游攻略数量柱状图', // 主标题
    subtext: '统计维度：攻略分类', // 副标题
    left: 'center'
  },
  grid: { // 增加这个属性，bottom就是距离底部的距离

    bottom: '20%'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 示例数据：统计的维度（横）
    name: '用户昵称',
    axisLabel: {
      show: true, // 是否显示刻度标签，默认显示
      interval: 0, // 坐标轴刻度标签的显示间隔，在类目轴中有效；默认会采用标签不重叠的策略间
      rotate: -60, // 刻度标签旋转的角度，在类目轴的类目标签显示不下的时候可以通过旋转防止标
      inside: false, // 刻度标签是否朝内，默认朝外
      margin: 6 // 刻度标签与轴线之间的距离
    }
  },
  yAxis: {
    type: 'value',
    name: '攻略数量'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'bar',
      itemStyle: {
        normal: {
          color: function () {
            return "#" + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16);          }
        }
      }
    }
  ]
};
// 饼图
let pieOption = {
  title: {
    text: '帖子分类统计',
    subtext: '攻略分类', // 副标题
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量',
      type: 'pie',
      radius: '50%',
      data: [
        { value: 1048, name: 'Search Engine' },
        { value: 735, name: 'Direct' },
        { value: 580, name: 'Email' },
        { value: 484, name: 'Union Ads' },
        { value: 300, name: 'Video Ads' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}
</script>