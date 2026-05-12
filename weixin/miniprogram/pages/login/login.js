var api = require('../../api/index.js');

Page({
  data: {
    mode: 'login',
    username: '',
    password: '',
    nickname: '',
    phone: '',
    loading: false,
    wxLoading: false
  },

  switchMode: function(e) {
    var mode = e.currentTarget.dataset.mode;
    this.setData({ mode: mode, username: '', password: '', nickname: '', phone: '' });
  },

  onUsernameInput: function(e) { this.setData({ username: e.detail.value }); },
  onPasswordInput: function(e) { this.setData({ password: e.detail.value }); },
  onNicknameInput: function(e) { this.setData({ nickname: e.detail.value }); },
  onPhoneInput: function(e) { this.setData({ phone: e.detail.value }); },

  onWxLogin: function() {
    if (this.data.wxLoading) return;
    this.setData({ wxLoading: true });
    var self = this;

    wx.login({
      success: function(loginRes) {
        if (!loginRes.code) {
          wx.showToast({ title: '微信登录失败', icon: 'none' });
          self.setData({ wxLoading: false });
          return;
        }
        var code = loginRes.code;

        // 尝试获取用户头像昵称（用户可以拒绝，不影响登录）
        wx.getUserProfile({
          desc: '用于展示您的头像和昵称',
          success: function(profileRes) {
            self._doWxLogin(code, profileRes.userInfo.nickName, profileRes.userInfo.avatarUrl);
          },
          fail: function() {
            self._doWxLogin(code, null, null);
          }
        });
      },
      fail: function() {
        wx.showToast({ title: '微信登录失败', icon: 'none' });
        self.setData({ wxLoading: false });
      }
    });
  },

  _doWxLogin: function(code, nickname, avatarUrl) {
    var self = this;
    api.wxLogin({ code: code, nickname: nickname, avatarUrl: avatarUrl }).then(function(res) {
      var userVO = res.data;
      var app = getApp();
      app.setUserData(userVO.token, userVO.id, userVO);
      self.setData({ wxLoading: false });
      wx.showToast({ title: '登录成功', icon: 'success' });
      setTimeout(function() {
        wx.switchTab({ url: '/pages/index/index' });
      }, 800);
    }).catch(function(err) {
      console.error('[wx-login] failed', JSON.stringify(err));
      self.setData({ wxLoading: false });
    });
  },

  onSubmit: function() {
    if (this.data.loading) return;

    var mode = this.data.mode;
    var username = this.data.username;
    var password = this.data.password;
    var nickname = this.data.nickname;
    var phone = this.data.phone;

    if (!username || !username.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' });
      return;
    }
    if (!password || !password.trim()) {
      wx.showToast({ title: '请输入密码', icon: 'none' });
      return;
    }
    if (mode === 'register' && password.length < 6) {
      wx.showToast({ title: '密码长度不能少于6位', icon: 'none' });
      return;
    }

    this.setData({ loading: true });

    var self = this;
    var call = mode === 'login'
      ? api.login({ username: username, password: password })
      : api.register({ username: username, password: password, nickname: nickname || null, phone: phone || null });

    call.then(function(res) {
      var userVO = res.data;
      var app = getApp();
      app.setUserData(userVO.token, userVO.id, userVO);
      self.setData({ loading: false });
      wx.showToast({ title: mode === 'login' ? '登录成功' : '注册成功', icon: 'success' });
      setTimeout(function() {
        wx.switchTab({ url: '/pages/index/index' });
      }, 800);
    }).catch(function(err) {
      console.error('[login] request failed', JSON.stringify(err));
      self.setData({ loading: false });
    });
  }
});
