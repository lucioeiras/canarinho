<template>
  <div class="explore">
    <img src="../assets/canarinho.svg" />
    <CreatePost />
    <Tabs v-bind="{ tab: 'Profile' }"/>
    <div class="row">
      <div class="info">
        <h2>{{ user.name }}</h2>
        <span>{{ user.email }}</span>
      </div>

      <button @click="onClick">
        {{ isLoggedAccount ? 'Deletar Conta' : isFollowing ? 'Deixar de Seguir' : 'Seguir' }}
      </button>
    </div>

    <ul><Post
      :key="post"
      v-for="post in posts"
      v-bind="{
        content: post.content,
        author: { id: user.id, name: user.name },
        createdAt: post.createdAt
      }"
    /></ul>
  </div>
</template>

<script>
  import { user } from '../contexts/user'
  import { api } from '../config/axios'

  import CreatePost from '../components/CreatePost.vue'
  import Tabs from '../components/Tabs.vue'
  import Post from '../components/Post.vue'

  export default {
    data() {
      return {
        isLoggedAccount: false,
        isFollowing: false,
        user: {},
        posts: {},
      }
    },
    async beforeCreate() {
      this.user = await user.get(this.$route.params.id)

      if (this.user.id === user.id) {
        this.isLoggedAccount = true
      } else {
        const response = await api.get(`/user/following/${user.id}`)
        const following = response.data

        this.isFollowing = !!following.find(
          followingUser => followingUser.user.id === this.user.id
        )
      }

      const response = await api.get(`/post/user/${this.user.id}`)
      this.posts = response.data.map(data => data.post).reverse()
    },
    methods: {
      follow() {
        api.post(
          '/user/follow', { followerId: user.id, followedId: this.user.id
        })

        this.isFollowing = true
      },
      unfollow() {
        api.delete(
          '/user/unfollow', { data: {
            followerId: user.id, followedId: this.user.id
          }}
        )

        this.isFollowing = false
      },
      deleteAccount() {
        user.delete(`/user/${user.id}`)
        this.$router.push('/signup')
      },
      onClick() {
        this.isLoggedAccount
          ? this.deleteAccount() : this.isFollowing
            ? this.unfollow() : this.follow()
      }
    },
    components: { CreatePost, Tabs, Post }
  }
</script>

<style scoped>
  .explore {
    width: 100%;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    padding: 40px 32px;
  }

  .info {
    width: 100%;
    margin-top: 24px;
  }

  .info h2 {
    color: #2D3748;
    margin-bottom: 8px;
  }

  img {
    height: 24px;
    margin-bottom: 32px;
  }

  ul {
    width: 100%;
  }
</style>
