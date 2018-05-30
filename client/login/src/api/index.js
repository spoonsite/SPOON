import axios from 'axios'
import store from '../store'

export default {
  getTest () {
    return axios.get('/').then(response => {
      console.log(response.data)
      store.commit('setTitle', { title: 'SPOONY IS Cool' + new Date() })
    })
  },
  getFAQquestions () {
    return this.$http
      .get('/openstorefront/api/v1/resource/faq')
      .then(response => {
        var filtered = response.data.filter(item => item.activeStatus === 'A')
        console.log(filtered)
        return filtered
      })
  }
}
