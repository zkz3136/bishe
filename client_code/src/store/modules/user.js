import toolUtil from '@/utils/toolUtil.js'
import config from '@/utils/config.js'
import http from '@/utils/http.js'
import default_avatar from "@/assets/avatar.png"

export default {
    namespaced: true,
    state:{
        session:{},
        username:'管理员'
    },
    getters:{
        session(state){
            return state.session
        },
        avatar(state){
            let key;
            if(toolUtil.storageGet('frontSessionTable') == 'yonghu'){
                key = 'touxiang'
            }
            if(toolUtil.storageGet('frontSessionTable') == 'users'){
                key = 'tupian'
            }
            if(toolUtil.storageGet('frontSessionTable') == 'shangjia'){
                key = 'touxiang'
            }
            let avatar = state.session[key]
            return avatar?config.get().url+avatar:default_avatar
        },
    },
    actions:{
        async getSession({commit}){
            let res = await http.get(`${toolUtil.storageGet('frontSessionTable')}/session`)
            if(res.data.code==0){
                commit('set_session',res.data.data)
            }
            return res
        },
        async update({commit},data){
            let res = await http.post(`${toolUtil.storageGet('frontSessionTable')}/update`,data)
            if(res.data.code==0){
                commit('assign_session',data)
            }
            return res
        },
    },
    mutations:{
        set_session(state,data){
            state.session = data
        },
        assign_session(state,data){
            Object.assign(state.session,JSON.parse(JSON.stringify(data)))
        },
    }
}