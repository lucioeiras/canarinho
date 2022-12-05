<template>
  <div class="signUp">
    <img src="../assets/canarinho.svg" />
    <h1>Crie sua conta</h1>
    <form @submit.prevent="onSubmit">
      <input v-model="name" type="text" placeholder="Digite seu nome" />
      <input v-model="email" type="email" placeholder="Digite seu e-mail" />
      <input v-model="password" type="password" placeholder="Digite sua senha" />

      <button type="submit">Criar Conta</button>
    </form>
  </div>
</template>

<script>
import { user } from '../contexts/user'

export default {
  data() {
    return {
      name: '',
      email: '',
      password: ''
    }
  },
  methods: {
    onSubmit() {
      user.change(this.name, this.email, this.password)
      this.$router.push('/explore')
    }
  }
}
</script>

<style>
  .signUp {
    height: 100vh;
    width: 100%;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    padding: 0px 48px 80px 48px;
  }

  img {
    height: 24px;
    margin-bottom: 32px;
  }

  h1 {
    color: #1A202C;
    font-weight: 600;
  }

  form {
    width: 100%;

    display: flex;
    flex-direction: column;
    align-items: center;

    margin-top: 16px;
  }

  input {
    width: 100%;

    border: 1px solid #E2E8F0;
    border-radius: 8px;

    background: #F7FAFC;
    color: #4A5568;

    padding: 16px;
    margin-top: 16px;
  }

  input::placeholder {
    color: #A0AEC0;
  }

  button {
    cursor: pointer;

    width: 100%;

    border: none;
    border-radius: 5px;

    background: #4FD1C5;
    color: #fff;
    font-weight: 700;

    padding: 16px;
    margin-top: 24px;

    transition: background-color 0.5s;
  }

  button:hover {
    background: #319795;
  }
</style>
