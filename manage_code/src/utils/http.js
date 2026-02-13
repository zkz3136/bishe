import axios from 'axios'
import router from '../router/index'
import toolUtil from '@/utils/toolUtil'
import config from '@/utils/config'
import { ElMessage } from 'element-plus'

let authRedirecting = false

const redirectToLogin = (message) => {
    if (authRedirecting) return
    authRedirecting = true
    toolUtil.storageClear()
    ElMessage.error({
        message: message || '未登录或登录过期',
        grouping: true,
        repeatNum: -99,
    })
    try {
        const base = window.location.href.split('#')[0]
        window.location.replace(`${base}#/login`)
    } catch (e) {
        router.replace('/login')
    } finally {
        setTimeout(() => {
            authRedirecting = false
        }, 800)
    }
}

const http = axios.create({
    timeout: 1000 * 86400,
    withCredentials: true,
    baseURL: process.env.VUE_APP_BASE_API,
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
})
// 请求拦截
http.interceptors.request.use(config => {
    config.headers['Token'] = toolUtil.storageGet('Token') // 请求头带上token
    return config
}, error => {
    return Promise.reject(error)
})
// 响应拦截
http.interceptors.response.use(response => {
    const camelToSnake = (key) => key.replace(/([A-Z])/g, '_$1').toLowerCase()
    const transformKeysDeep = (obj) => {
        if (obj === null || obj === undefined) return obj
        if (Array.isArray(obj)) return obj.map(i => transformKeysDeep(i))
        if (Object.prototype.toString.call(obj) !== '[object Object]') return obj
        const out = {}
        Object.keys(obj).forEach(k => {
            const v = obj[k]
            const nk = camelToSnake(k)
            if (out.hasOwnProperty(nk)) {
                out[nk] = out[nk]
            } else {
                out[nk] = transformKeysDeep(v)
            }
        })
        return out
    }
    if (response.data && response.data.code == 401) { // 401, token失效
        redirectToLogin(response.data.msg)
        return new Promise(() => {})
	}
	else if(response.data && response.data.code == 0){
        if (response.data.data && typeof response.data.data === 'object') {
            const d = response.data.data
            if (d.list && Array.isArray(d.list)) {
                d.list = d.list.map(item => transformKeysDeep(item))
            } else {
                response.data.data = transformKeysDeep(d)
            }
            if (d.total === undefined && d.totalCount !== undefined) {
                d.total = d.totalCount
            }
            if (d.page === undefined && d.currPage !== undefined) {
                d.page = d.currPage
            }
            if (d.limit === undefined && d.pageSize !== undefined) {
                d.limit = d.pageSize
            }
        }
		return response
	}else{
		ElMessage.error({
            message:response.data.msg || '请求失败',
            grouping:true,
            repeatNum:-99,
        })
		return Promise.reject(response)
	}
    
}, error => {
    const code = error?.response?.data?.code
    if (code == 401) {
        redirectToLogin(error?.response?.data?.msg)
        return new Promise(() => {})
    }
    return Promise.reject(error)
})
export default http
