<template>
  <form @submit="onSubmit">
    <textarea
      name="post"
      placeholder="O que você está pensando?"
      v-model="content"
    ></textarea>
    <button type="submit">Criar Post</button>
  </form>
</template>

<script>
import { api } from '../config/axios'
import { user } from '../contexts/user'

export default {
  data() {
    return {
      content: ''
    }
  },
  methods: {
    onSubmit() {
      api.post('/post', { post: { authorId: user.id, content: this.content } })
      this.content = ''
    }
  }
}
</script>

<style scoped>
  textarea {
    width: 100%;

    border: 1px solid #E2E8F0;
    border-radius: 8px;

    background: #F7FAFC;
    color: #4A5568;
    font-size: 14px;

    padding: 16px 16px 48px 16px;
  }

  textarea::placeholder {
    color: #A0AEC0;
  }

  button {
    cursor: pointer;

    width: 100%;

    border: none;
    border-radius: 5px;

    background: rgba(79, 209, 197, 0.24);
    color: #4FD1C5;
    font-weight: 700;
    font-size: 14px;

    padding: 16px;
    margin-top: 16px;

    transition: 0.5s;
  }

  button:hover {
    background: #4FD1C5;
    color: #FFF;
  }
</style>
