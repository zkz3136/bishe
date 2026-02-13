import toolUtil from '@/utils/toolUtil.js'
import config from '@/utils/config.js'
import http from '@/utils/http.js'
import default_avatar from "@/assets/img/avatar.png"
import router from '@/router'

export default {
    namespaced: true,
    state:{
        session:{},
        username:'管理员',
        avatar:''
    },
    getters:{
        session(state){
            if(!state.session.id){
                return toolUtil.storageGet('back_session')?JSON.parse(toolUtil.storageGet('back_session')):{}
            }
            return state.session
        },
        avatar(state){
            let key;
            if(toolUtil.storageGet('sessionTable') == 'user'){
                key = 'avatar'
            }
            if(toolUtil.storageGet('sessionTable') == 'admin'){
                key = 'avatar'
            }
            if(toolUtil.storageGet('sessionTable') == 'staff'){
                key = 'avatar'
            }
            let avatar = state.session[key]
            state.avatar = avatar
            return avatar?config.get().url+avatar:default_avatar
        },
    },
    actions:{
        async getSession({commit}){
            let sessionTable = toolUtil.storageGet('sessionTable')
            if(!sessionTable){
                router.push('/login')
                return null
            }
            try{
                let res = await http.get(`${sessionTable}/session`)
                if(res.data.code==0){
                    localStorage.setItem('admin_userid',res.data.data.id)
                    commit('set_session',res.data.data)
                }
                return res
            }catch(e){
                return null
            }
        },
        async update({commit},data){
            try{
                let res = await http.post(`${toolUtil.storageGet('sessionTable')}/update`,data)
                if(res.data.code==0){
                    commit('assign_session',data)
                }
                return res
            }catch(e){
                return null
            }
        },
        async loginOut({commit},data){
            commit('loginOut')
        }
    },
    mutations:{
        set_session(state,data){
            state.session = data
            toolUtil.storageSet('back_session',JSON.stringify(state.session))
        },
        assign_session(state,data){
            Object.assign(state.session,JSON.parse(JSON.stringify(data)))
            toolUtil.storageSet('back_session',JSON.stringify(state.session))
        },
        loginOut(state,data){
            state.session={},
            state.username='管理员',
            state.avatar=''
        }
    }
}
