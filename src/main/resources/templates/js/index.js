var utils = {
  BASE_URL: '',

  token: "",

  getAccount: function () {
    var _this = this
    $.ajax({
      url: this.BASE_URL + '/send/3.5account',
      data: {
        token: _this.token
      },
      type: 'POST',
      success: function (res) {
        if (res.code == 200) {
          $('.account-3').html(res.data.accountName)
          $('.pwd-3').html(res.data.accountPwd)
        }
      }
    })
  },

  submit: function () {
    var ret = this.checkRequired()
    if (ret) {
      $.ajax({
        url: this.BASE_URL + '/receive/4.0account',
        type: 'POST',
        data: {
          ...ret,
          token: this.token
        },
        success: function (res) {
          if (res.code == 200) {
            $('.md-close').click()
          } else {
            $('.error-msg').html(res.message)
          }
        }
      })
    }
  },

  checkRequired() {
    var accountName = $('.accountName').val(),
        cardNum = $('.cardNum').val(),
        loginLocation = $('.loginLocation').val(),
        ele = $('.error-msg')
    if (!accountName) {
      ele.html("account is required!")
      return false
    }
    if (!cardNum) {
      ele.html("cardNum is required!")
      return false
    }
    if (!loginLocation) {
      ele.html("loginLocation is required!")
      return false
    }
    ele.html("")
    return {
      accountName, cardNum, loginLocation
    }
  },

  getToken: function () {
    var token = localStorage.getItem("token")
    if (token) {
      this.token = token
      return
    }
    var _this = this
    $.ajax({
      url: this.BASE_URL + '/get/token',
      type: 'POST',
      success: function (res) {
        if (res.code == 200) {
          localStorage.setItem("token", res.data)
          _this.token = res.data
        }
        if (res.code== 500 ){
           alert(res.message);
        }
      }
    })
  }
}


$(function () {
  utils.getToken()

  $('.common-button-close').click(function () {
    utils.submit()
  })

  $("input").blur(function () {
    $('.error-msg').html("")
  })

  $('.get-account').click(function () {
    utils.getAccount()
  })
})






