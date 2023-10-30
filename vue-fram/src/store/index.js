import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //判断是否登录
    isLogin: localStorage.getItem("token") || false,
    //默认头像
    avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
    resultImg: '',   //本地上传的图片
    search: '',   //搜索的内容
    selectAddress: '', //选择的地址id
    shopCarIds: [], //选择的购物车id
    searchGoods: '', //搜索的商城内容
    auth: false, //实名状态
    activeMeau: '',
  },
  // getters: {
  //   address(state) {
  //     return state.selectAddress
  //   }
  // },
  mutations: {
    ISLOGIN(state){
      state.isLogin = !state.isLogin
    },
    AVATAR(state,value) {
      state.avatar = value
    },
    RESULTIMG(state,imgUrl) {
      state.resultImg = imgUrl
    },
    SEARCH(state,value) {
      state.search = value
    },
    SELECTADD(state,value) {
      state.selectAddress = value
    },
    CLEARSELECT(state) {
      state.selectAddress = ''
    },
    SHOPCARIDS(state,value) {
      state.shopCarIds = value
    },
    SEARCHGOODS(state,value) {
      state.searchGoods = value
    },
    AUTH(state) {
      state.auth = true
    },
    NOAUTH(state) {
      state.auth = false
    },
    SETACTIVE(state,value) {
      state.activeMeau = value
    }
  },

  actions: {
    islogin(content){
      content.commit("ISLOGIN")
    },
    avatar(content,value) {
      content.commit("AVATAR",value)
    },
    resultimg(content,imgUrl) {
      content.commit("RESULTIMG",imgUrl)
    },
    search(content,value) {
      content.commit("SEARCH",value)
    },
    selectadd(content,value) {
      content.commit('SELECTADD',value)
    },
    clearselect(content) {
      content.commit('CLEARSELECT')
    },
    shopcarids(content,value) {
      content.commit('SHOPCARIDS',value)
    },
    searchgoods(content,value) {
      content.commit('SEARCHGOODS',value)
    },
    auth(content) {
      content.commit('AUTH')
    },
    noauth(content) {
      content.commit('NOAUTH')
    },
    setactive(content,value) {
      content.commit('SETACTIVE',value)
    }
  },
  
})
