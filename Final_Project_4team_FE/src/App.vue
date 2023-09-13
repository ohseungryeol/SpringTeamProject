<template>
  <div id="app">
    <nav-bar />
    <router-view />
  </div>
</template>

<script>
import NavBar from "@/components/layout/navbar.vue";
export default {
  name : "App",
  components:{
    NavBar,
  },
};
</script>

<style>
/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
} */
/* #app {
  font-family: 'myfont';
  src :
  color: #2c3e50;
} */
@font-face {
  font-family: 'twayair';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_tway@1.0/twayair.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&family=Shadows+Into+Light&display=swap');

#app {
  font-family: 'Noto Sans KR';
  color: #2c3e50;
}

.btn{
  font-size : 0.5em;
  padding: 0.5em;
  border-radius : 2em;
}
</style>