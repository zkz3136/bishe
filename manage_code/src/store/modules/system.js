import toolUtil from '@/utils/toolUtil.js'
import config from '@/utils/config.js'
import http from '@/utils/http.js'

export default {
    namespaced: true,
    state:{
        notice:{}
    },
    getters:{

    },
    actions:{
        getSystemNotice({commit}){
            http.get('/system_notice/list?sort=id&order=desc&t=' + new Date().getTime()).then(res=>{
                commit('setNotice',res.data.data.list[0])
            })
        },
    },
    mutations:{
        setNotice(state,data){
            state.notice = data
        },
    }
}
