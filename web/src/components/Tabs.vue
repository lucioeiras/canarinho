<template>
  <div class="tabs">
    <router-link
      to="/explore" class="tab" :class="tab === 'Explore' && 'active'"
    >
      <p>Explorar</p>
      <div class="line" />
    </router-link>

    <router-link
      :to="`/profile/${userId}`" class="tab" :class="tab === 'Profile' && 'active'"
    >
      <p>Perfil</p>
      <div class="line" />
    </router-link>
  </div>
</template>

<script>
import { user } from '../contexts/user'

export default {
  data() {
    return {
      userId: user.id,
    }
  },
  props: {
    tab: String
  }
}
</script>

<style scoped>
  .tabs {
    width: 100%;

    display: flex;
    flex-direction: row;
    align-items: center;

    margin-top: 48px;
  }

  .tab {
    cursor: pointer;
    text-decoration: none;

    width: 100%;

    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .line {
    width: 100%;
    height: 2px;

    background: #EDF2F7;
  }

  p {
    color: #A0AEC0;
    font-weight: 500;
    font-size: 14px;

    margin-bottom: 16px;
  }

  .active .line {
    background: #4FD1C5;
  }

  .active p {
    color: #2D3748;
    font-weight: 700;
  }
</style>
