import axios from 'axios'
import store from '../store'

export default {
  getTest () {
    return axios.get('/').then(response => {
      console.log(response.data)
      store.commit('setTitle', { title: 'SPOONY IS Cool' + new Date() })
    })
  }
}
