import { createRouter, createWebHistory } from 'vue-router'

import Explore from '../views/Explore.vue'
import SignUp from '../views/SignUp.vue'
import Profile from '../views/Profile.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/explore',
      name: 'explore',
      component: Explore
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignUp
    },
    {
      path: '/profile/:id',
      name: 'profile',
      component: Profile
    },
  ]
})

export default router
