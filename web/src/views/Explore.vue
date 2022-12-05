<template>
  <div class="explore">
    <img src="../assets/canarinho.svg" />
    <CreatePost />
    <Tabs v-bind="{ tab: 'Explore' }"/>
    <ul><Post
      :key="data"
      v-for="data in posts"
      v-bind="{
        content: data.post.content,
        author: { id: data.post.authorId, name: data.user.name },
        createdAt: data.post.createdAt
      }"
    /></ul>
  </div>
</template>

<script>
  import { api } from '../config/axios'

  import CreatePost from '../components/CreatePost.vue'
  import Tabs from '../components/Tabs.vue'
  import Post from '../components/Post.vue'

  export default {
    data() {
      return {
        posts: {},
      }
    },
    async beforeCreate() {
      const response = await api.get(`/post`)
      this.posts = response.data.reverse()
    },
    components: { CreatePost, Tabs, Post },
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

  img {
    height: 24px;
    margin-bottom: 32px;
  }

  ul {
    width: 100%;
  }
</style>
