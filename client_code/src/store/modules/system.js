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
            http.get('/system_notice/list?sort=id&order=desc').then(res=>{
                if(res.data.data.list.length){
                    commit('setNotice',res.data.data.list[0])
                }
            })
        },
    },
    mutations:{
        setNotice(state,data){
            state.notice = data
        },
    }
}
