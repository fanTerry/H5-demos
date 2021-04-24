<template>
  <div class="Page addressEdit_Page">
    <header class="mod_header">
      <navBar pageTitle='编辑地址'></navBar>
    </header>
    <div class="main">
      <writeAddress ref="address" @showDialog='showDelete=true' @successSave="successSave">
      </writeAddress>

    </div>

    <footer class="mod_footer">
      <a class="exchange_btn" @click="save()">保存</a>
    </footer>
    <alert class="alert" :type="'2'" :showPop="showDelete" @close='showDelete=false' @confirm="deletAddress">
      <p>是否删除地址</p>
    </alert>
  </div>
</template>

<script>
import navBar from "../../../components/header/nav_bar";
import writeAddress from "../../../components/pop_up/store/writeAddress.vue";
import alert from "../../../components/pop_up/pop_alert.vue";

export default {
  data() {
    return {
      showDelete: false,
      popAddressFlag: true,
      id: Number
    };
  },
  created() {},
  mounted() {
    if (this.$route.query.type == 1) {
      //新增
    } else {
      //修改
      this.$refs.address.address = this.$route.query.address;
    }
  },
  methods: {
    save() {
      this.$refs.address.submit();
    },
    successSave() {
      this.$router.go(-1);
    },
    deletAddress() {
      let param = {};
      param.addresId = this.$route.query.address.id;
      console.log("id", param);
      return this.$post("/api/shopAddress/deleteAddress", param)
        .then(rsp => {
          console.log(rsp);
          if (rsp.code === "200") {
            this.$router.go(-1);
          } else {
            this.$toast(rsp.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  components: {
    navBar,
    writeAddress,
    alert
  }
};
</script>

<style lang="scss">
.addressEdit_Page {
  .alert.ui_pop {
    position: fixed !important;
    background: rgba(0, 0, 0, 0.8) !important;
  }
  .main {
    position: relative;
  }
  .ui_pop {
    position: initial !important;
    background-color: #f5f4f3 !important;
  }

  .receive_adress {
    bottom: initial !important;
    top: 0;
    border-radius: 0 !important;
  }
  .close,
  .title,
  .location_address,
  .exchange_btn {
    display: none !important;
  }

  .delete_address {
    position: absolute;
    top: 305px;
    width: 100%;
    line-height: 48px;
    font-size: 14px;
    color: #333;
    background-color: #fff;
    text-align: center;
  }
}
</style>


<style lang='scss' scoped>
@import "../../../assets/common/_mixin";
@import "../../../assets/common/_base";
.Page {
  background-color: #fff;
  .mod_footer {
    .exchange_btn {
      @extend .flex_v_h;
      display: flex !important;
      margin: 4px auto;
      @include getBtn(210px, 40px,17px,#fff, transparent,  40px);
      background: linear-gradient(to right, #ffa200, #ff7b1c);
    }
  }
}
</style>
