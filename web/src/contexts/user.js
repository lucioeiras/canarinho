import { reactive } from 'vue'
import { v4 as uuid } from 'uuid'

import { api } from '../config/axios'

export const user = reactive({
  id: window.localStorage.getItem('id') || '',
  name: window.localStorage.getItem('name') || '',
  email: window.localStorage.getItem('email') || '',
  password: window.localStorage.getItem('password') || '',

  change(name, email, password) {
    this.id = uuid()
    this.name = name
    this.email = email
    this.password = password

    api.post('/user', {
      user: { id: this.id, name, email, password }
    })

    window.localStorage.setItem('id', this.id)
    window.localStorage.setItem('name', this.name)
    window.localStorage.setItem('email', this.email)
    window.localStorage.setItem('password', this.password)
  },

  async get(id) {
    const response = await api.get(`/user/${id}`)
    return response.data[0]
  },

  delete() {
    api.delete(`/user/${this.id}`)

    this.id = ''
    this.name = ''
    this.email = ''
    this.password = ''

    window.localStorage.clear()
  }
})
