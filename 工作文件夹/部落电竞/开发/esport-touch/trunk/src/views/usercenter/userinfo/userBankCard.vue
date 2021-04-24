<template>
  <div class="Page userAuthPage">
    <header class="mod_header">
      <navBar :pageTitle="canBindCard?'添加银行卡':'银行卡信息'"></navBar>
    </header>
    <div class="main">
      <section>
        <label>真实姓名</label>
        <input v-model="bankInfo.trueName" :disabled="!canBindCard" type="text" maxlength="20" placeholder="请输入银行卡对应的姓名" @blur="scrollToTop" />
      </section>
      <section>
        <label>所属银行</label>
        <input v-model="bankInfo.bankName" :disabled="!canBindCard" type="text" maxlength="20" placeholder="请输入所属银行" @blur="scrollToTop" />
      </section>
      <section>
        <label>分行信息</label>
        <input v-model="bankInfo.branchBank" :disabled="!canBindCard" type="text" placeholder="请输入分行信息" maxlength="18" @blur="scrollToTop" />
      </section>

      <section>
        <label>银行卡号</label>
        <input v-model="bankInfo.bankNo" :disabled="!canBindCard" type="tel" placeholder="请输入银行卡号" maxlength="25" @blur="scrollToTop" />
      </section>
      <a class="submit_btn" @click="toSubmit()" v-if="canBindCard">提交</a>
      <a class="submit_btn" @click="goback()" v-else>返回</a>

      <article class="bind_card_rules">
        <p>赢加余额退款说明:</p>
        <p>
          <span>请您仔细阅读以下信息后再进行银行卡绑定</span>
        </p>
        <br />
        <p>1.请您务必仔细检查填写信息，包括个人姓名，银行卡号，银行支行等信息，一经提交不可修改；</p>
        <p>2.一旦完成个人银行卡号填写，赢加个人账户余额将会冻结，无法进行任何货币操作；</p>
        <p>3.赢加余额退款采用人工手动打款方式进行，因此需一定的人工处理时间，平台将会努力保证在您填写完24小时内完成打款，请注意银行收款短信信息；</p>
        <p>4.由于个人银行卡信息填写错误导致的打款失败，平台将会有专人客服人员联系您进行信息修正，请保持手机开机，敬请留意来电；</p>
        <p>5.成功完成财务打款后，用户账户余额将清零，若您账户余额为0，说明已打款成功，敬请留意银行账户余额变动；</p>
      </article>
    </div>
  </div>
</template>

<script>
import navBar from '../../../components/header/nav_bar/index';
import { getCheck } from '../../../libs/utils';
import { mapMutations } from 'vuex';

export default {
  components: {
    navBar
  },
  data() {
    return {
      bankInfo: {
        trueName: '',
        bankName: '',
        branchBank: '',
        bankNo: ''
      },
      canBindCard: false
    };
  },
  created() {},
  mounted() {
    this.getUserBankCard();
  },
  methods: {
    getUserBankCard() {
      this.$post('/api/usercenter/getBankCard')
        .then(rsp => {
          let response = rsp.data;
          console.log(response.bankCard, 'response');
          if (rsp.code === '200') {
            this.canBindCard = response.canBindCard;
            if (!this.canBindCard && null != response.bankCard) {
              this.bankInfo.trueName = response.bankCard.bankAccountName;
              this.bankInfo.bankName = response.bankCard.bankName;
              this.bankInfo.branchBank = response.bankCard.bankBranch;
              this.bankInfo.bankNo = response.bankCard.bankAccountNo;
            }
          } else {
            this.$toast(rsp.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    toSubmit() {
      if (!this.bankInfo.trueName || !this.bankInfo.bankName || !this.bankInfo.branchBank || !this.bankInfo.bankNo) {
        this.$toast('必要参数为空');
        return;
      }
      this.$post('/api/usercenter/bindCard', this.bankInfo)
        .then(rsp => {
          let response = rsp.data;
          if (rsp.code === '200') {
            this.$toast('银行卡绑定成功!');
            setTimeout(() => {
              this.$router.go(-1);
            }, 3 * 1000);
          } else {
            this.$toast(rsp.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    goback() {
      this.$router.go(-1);
    },
    scrollToTop: function() {
      setTimeout(function() {
        window.scrollTo(0, 0);
        console.log('回滚');
      }, 100);
    }
  }
};
</script>


<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';
@import '../../../assets/common/_var';

section {
  position: relative;
  @extend .flex_hc;
  @include getBorder(bottom, rgba(0, 0, 0, 0.1));
  height: 44px;
  margin-top: 10px;
  padding: 0 20px;
  font-size: 14px;
  background-color: #fff;
  label {
    width: 65px;
    line-height: 44px;
  }
  input {
    flex: 1;
    -webkit-flex: 1;
    line-height: 44px;
    border: none;
  }
}
.personal_sign {
  align-items: flex-start;
  flex-direction: column;
  -webkit-flex-direction: column;
  height: initial;
  padding: 15px 20px;
  label {
    line-height: 1;
  }
  textarea {
    margin-top: 10px;
    width: 100%;
    padding-top: 5px;
    line-height: 16px;
    border: none;
    background-color: rgba(0, 0, 0, 0.05);
  }
}
.submit_btn {
  display: block;
  margin: 12px 16px 12px;
  padding: 15px;
  font-size: 18px;
  color: #fff;
  border-radius: 8px;
  text-align: center;
  background-color: $color_main;
}
.phonecode_div {
  background-color: #fff;
  padding-top: 10px;
  padding-bottom: 2px;
}
.tips {
  padding-top: 15px;
  // padding-bottom: 5px;
  padding-left: 20px;
  color: #da3a40;
  line-height: 1.5;
}

.bind_card_rules {
  margin: 12px 16px;
  p {
    font-size: 14px;
    line-height: 24px;
    color: #666;
    span {
      color: $color_main;
      font-weight: bold;
    }
  }
}
</style>
