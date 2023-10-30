import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        isCollapse: false, //控制侧边框收缩
        sideWide: 180,  //侧边宽度
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', //用户头像
        menuList: [],
        
    },
    getters: {
        getMenuList : state => {
            return state.menuList
        }
    },
    mutations: {
        //更改侧边框的收缩
        CHANGE(state) {
            console.log("mutations中的change方法被调用了")
            state.isCollapse = !state.isCollapse;
        },
        //更改侧边框大小
        CHANGESIDE(state, value) {
            state.sideWide = value;
        },
        // 更换头像
        CHANGEAVATAR(state,value) {
            state.avatar = value;
        },
        //清空vuex中的数据
        CLEAR(state) {
            localStorage.clear("EmpPerm")
            state.menuList = []
        },
        //获取localStorage中的数据
        GETMSG(state) {
            state.menuList = JSON.parse(localStorage.getItem('EmpPerm')) 
        },
       

    },

    actions: {
        change(context) {
            context.commit('CHANGE')
        },
        changeside(context, value) {
            context.commit('CHANGESIDE', value)
        },
        changeavatar(context,value) {
            context.commit('CHANGEAVATAR',value)
        },
        clear(context) {
            context.commit('CLEAR')
        },
        getmsg(context) {
            context.commit('GETMSG')
        },
       
    },

})
