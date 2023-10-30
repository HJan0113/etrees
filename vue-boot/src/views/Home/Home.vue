<template>
  <div>
    <el-row :gutter="30" style="margin-bottom: 40px">
      <el-col :span="6">
        <el-card style="color: cornflowerblue">
          <div><i class="el-icon-user-solid" />
            识别数量 <el-tag>日</el-tag>
          </div>
          <el-divider></el-divider>
          <div class="count">
            {{ HomeData.recordCount }}
          </div>
          <el-divider></el-divider>
          <div class="msg">
            <div class="key">总识别数量</div>
            <div class="number">{{ HomeData.t_recordCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-money" />
            反馈数量 <el-tag>日</el-tag>
          </div>
          <el-divider></el-divider>
          <div class="count">
            {{ HomeData.feedbackCount }}
          </div>
          <el-divider></el-divider>
          <div class="msg">
            <div class="key">总反馈数量</div>
            <div class="number">{{ HomeData.t_feedbackCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-bank-card" />
            订单数量 <el-tag>日</el-tag>
          </div>
          <el-divider></el-divider>
          <div class="count">
            {{ HomeData.orderCount }}
          </div>
          <el-divider></el-divider>
          <div class="msg">
            <div class="key">总订单数量</div>
            <div class="number">{{ HomeData.t_orderCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #E6A23C">
          <div><i class="el-icon-s-shop" />
            未处理订单 <el-tag>总</el-tag>
          </div>
          <el-divider></el-divider>
          <div class="count">
            {{ HomeData.pending }}
          </div>
          <el-divider></el-divider>
          <div class="msg">
            <div style="height: 15px;"></div>
            <div class="number"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: -20px;" :gutter="30">
      <el-col :span="18">
        <el-card>
          <div class="tabbar">
            <div>
              <i class="el-icon-s-marketing" />
              <span class="trend">识别趋势(近七天)</span>
            </div>
            <span class="day">今日识别数量: {{ HomeData.recordCount }}</span>
            <span class="rank">商品销售排行</span>
          </div>
          <el-divider></el-divider>
          <div class="map">
            <div ref="line" style="flex: 2;height: 330px;"></div>
            <div ref="bar" style="flex: 1;height: 330px;"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="tabbar">
            <div>
              <i class="el-icon-s-data" />
              <span>图鉴信息 <el-tag>总</el-tag></span>
            </div>
          </div>
          <el-divider></el-divider>
          <div class="des">
            <div class="data">
              <div class="text1">
                {{ HomeData.dataInfoCount }}
              </div>
              数据总量
            </div>
            <div class="data">
              <div class="text2">
                {{ HomeData.plantCount }}
              </div>
              植物种类
            </div>
            <div class="data">
              <div class="text2">
                {{ HomeData.diseaseCount }}
              </div>
              病种数量
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getHome, getBarData } from '@/api';
import * as echarts from 'echarts'
export default {
  data() {
    return {
      HomeData: {},
      lineData: [],
      barData: []
    }
  },
  mounted() {
    this.getAll()
    // this.getline()
    this.getbar()

  },
  methods: {
    getAll() {
      getHome().then(res => {
        this.HomeData = res.data
        this.lineData = res.map.mapList

        const line = echarts.init(this.$refs.line)
        let option = {
          grid: {
            top: 30
          },
          xAxis: {
            type: 'category',
            data: this.lineX
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.lineNum,
              type: 'line',
              label: {
                show: true,
                position: 'top',
                textStyle: {
                  fontSize: 10
                }
              }
            }
          ]
        };
        line.setOption(option);
      }),

        getBarData().then(res => {
          console.log("返回柱状图: ", res.data)
          this.barData = res.data
          const bar = echarts.init(this.$refs.bar)
          let option = {
            grid: {
              top: 30
            },
            xAxis: {
              type: 'category',
              data: this.barX,
              axisLabel: {
                interval: 0, // 所有标签都显示
                // rotate: 30, // 旋转30度
                margin: 15 // 标签与坐标轴的距离
              }
            },
            yAxis: {
              type: 'value',
              // axisLabel: {
              //   show: false
              // }
            },
            series: [
              {
                data: this.barNum,
                type: 'bar',
                label: {
                  show: true,
                  position: 'top'
                }
              }
            ]
          };
          bar.setOption(option)
        })
    },
    //折线图
    // getline() {
    //   const line = echarts.init(this.$refs.line)
    //   let option = {
    //     grid: {
    //       top: 30
    //     },
    //     xAxis: {
    //       type: 'category',
    //       data: []
    //     },
    //     yAxis: {
    //       type: 'value'
    //     },
    //     series: [
    //       {
    //         data: [],
    //         type: 'line',
    //         label: {
    //           show: true,
    //           position: 'top',
    //           textStyle: {
    //             fontSize: 10
    //           }
    //         }
    //       }
    //     ]
    //   };
    //   console.log("cishi ", this.lineData)
    //   console.log("lineNum =  ", this.lineX)
    //   console.log("linex =  ", this.lineX)
    //   option.xAxis.data = this.lineX
    //   option.series.data = this.lineNum
    //   line.setOption(option);
    // },
    //柱状图
    getbar() {

    }
  },
  computed: {
    lineX() {
      const date = this.lineData.map(res => {
        const result = res.date.split('-')
        return result[2]
      })
      return date
    },
    lineNum() {
      const count = this.lineData.map(res => {
        return res.count
      })
      return count
    },
    barX() {
      const name = this.barData.map(res => {
        const obj = Object.values(res)
        return obj[0].name
      })
      return name
    },
    barNum() {
      const num = this.barData.map(res => {
        const obj = Object.values(res)
        return obj[0].count
      })
      return num
    }
  }

}
</script>
<style scoped>
.el-card>>>.el-card__body {
  padding: 10px
}

div /deep/.el-divider--horizontal {
  margin: 12px 0;
}

.count {
  padding: 8px 0;
  text-align: center;
  font-weight: bold;
  font-size: 30px;
}

.msg {
  display: flex;
  font-size: 7px;
  justify-content: space-between;
}

.number {
  align-self: center;
  justify-self: flex-end;
}

.key {
  justify-self: flex-start;
}

.tabbar {
  display: flex;
}

.trend {
  flex: 1;
}

.day {
  flex: 1;
  margin-left: 180px;
}

.rank {
  flex: 1;
  margin-left: 60px;
}

.map {
  display: flex;
}

.des {
  display: flex;
  height: 330px;
  flex-direction: column;
  align-items: center;
}

.data {
  width: 100px;
  height: 100px;
  text-align: center;
  align-content: center;
}

.text1 {
  width: 90px;
  height: 70px;
  border-bottom: 1px solid #ddd6c1;
  padding-top: 27px;
  position: relative;
  font-size: 20px;
}

.text1::after {
  content: "条";
  position: absolute;
  bottom: 0;
  right: 0;
  font-size: 15px;
}

.text2 {
  width: 90px;
  height: 70px;
  padding-top: 27px;
  border-bottom: 1px solid #ddd6c1;
  position: relative;
  font-size: 20px;
}

.text2::after {
  content: "种";
  position: absolute;
  bottom: 0;
  right: 0;
  font-size: 15px;

}
</style>
