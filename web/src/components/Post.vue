<template>
  <li class="post">
    <p>{{ content }}</p>

    <div class="row">
      <span>
        Por
        <router-link :to="`/profile/${author.id}`">
          {{ author.name }}
        </router-link>
      </span>
      <span>{{ parsedCreatedAt }}</span>
    </div>
  </li>
</template>

<script>
export default {
  data() {
    return {
      parsedCreatedAt: '04/12/2022'
    }
  },
  props: {
    content: '',
    author: { id: '', name: '' },
    createdAt: ''
  },
  // mounted() {
  //   const createdAtDate = new Date(this.createdAt)
  //   this.parsedCreatedAt =
  //     createdAtDate.getDay() + '/' +
  //     createdAtDate.getMonth() + '/' +
  //     createdAtDate.getFullYear()
  // }
}
</script>

<style>
  .post {
    list-style: none;

    width: 100%;

    border: 1px solid #E2E8F0;
    border-radius: 8px;

    padding: 24px;
    margin-top: 24px;
  }

  p {
    color: #2D3748;
    margin-bottom: 24px;
  }

  .row {
    width: 100%;

    display: flex;
    justify-content: space-between;
  }

  span {
    color: #718096;
    font-size: 14px;
  }

  span a {
    text-decoration: none;
    color: #4A5568;
    font-weight: 600;
  }
</style>
