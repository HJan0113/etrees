<template>
  <div class="cont">
    <div class="btns">
      <a href="#" class="sign-in" v-bind:class="{ active: activeTab === 'sign-in-sec' }"
        v-on:click.prevent="activeTab = 'sign-in-sec'" >Sign in</a>
      <a href="#" class="sign-up" v-bind:class="{ active: activeTab === 'sign-up-sec' }"
        v-on:click.prevent="activeTab = 'sign-up-sec'" >Sign up</a>
    </div>
    <div class="sec sign-in-sec" v-show="activeTab === 'sign-in-sec'">Sign in form here</div>
    <div class="sec sign-up-sec" v-show="activeTab === 'sign-up-sec'">Sign up form here</div>
    <form id="form" v-on:submit.prevent="checkInputs">
      <input type="text" id="f-name">
      <input type="email" id="email">
      <input type="password" id="password">
      <input type="password" id="r-password">
      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'sign-in-sec',
    } 
  },
}
</script>

<style scoped>
.container {
  width: 50rem;
  background-color: pink;
}
</style>