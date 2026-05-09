var api = require('../../api/index.js');

Page({
  data: {
    mode: 'login',
    username: '',
    password: '',
    nickname: '',
    phone: '',
    loading: false
  },

  switchMode: function(e) {
    var mode = e.currentTarget.dataset.mode;
    this.setData({ mode: mode, username: '', password: '', nickname: '', phone: '' });
  },

  onUsernameInput: function(e) { this.setData({ username: e.detail.value }); },
  onPasswordInput: function(e) { this.setData({ password: e.detail.value }); },
  onNicknameInput: function(e) { this.setData({ nickname: e.detail.value }); },
  onPhoneInput: function(e) { this.setData({ phone: e.detail.value }); },

  onSubmit: function() {
    console.log('[login] onSubmit called, mode=' + this.data.mode + ' loading=' + this.data.loading);

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
    console.log('[login] sending request, username=' + username);

    var self = this;
    var call = mode === 'login'
      ? api.login({ username: username, password: password })
      : api.register({ username: username, password: password, nickname: nickname || null, phone: phone || null });

    call.then(function(res) {
      console.log('[login] request success', JSON.stringify(res.data));
      try {
        var userVO = res.data;
        var app = getApp();
        app.setUserData(userVO.token, userVO.id, userVO);
        self.setData({ loading: false });
        wx.showToast({ title: mode === 'login' ? '登录成功' : '注册成功', icon: 'success' });
        setTimeout(function() {
          wx.switchTab({ url: '/pages/index/index' });
        }, 800);
      } catch (e) {
        console.error('[login] inner error', e);
        self.setData({ loading: false });
      }
    }).catch(function(err) {
      console.error('[login] request failed', JSON.stringify(err));
      self.setData({ loading: false });
    });
  }
});
