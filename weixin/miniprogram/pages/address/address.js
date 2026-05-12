Page({
  data: {
    addresses: [],
    userId: null,
    from: null,
    action: null,
    editingId: null,
    regionArray: [],
    regionText: '请选择省市区',
    formData: {
      consignee: '',
      phone: '',
      province: '',
      city: '',
      district: '',
      detail: '',
      isDefault: 0
    }
  },

  onLoad(options) {
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    this.setData({ userId });

    if (options.from) {
      this.setData({ from: options.from });
    }
    if (options.action) {
      this.setData({ action: options.action });
      if (options.action === 'add') {
        this.initFormData();
      } else if (options.action === 'edit' && options.id) {
        this.setData({ editingId: parseInt(options.id) });
        this.loadAddressesForEdit(parseInt(options.id));
      }
    } else {
      this.loadAddresses();
    }
  },

  onShow() {
    const app = getApp();
    const userId = app.globalData.userId || wx.getStorageSync('userId');
    this.setData({ userId });
    if (userId) {
      this.loadAddresses();
    }
  },

  initFormData() {
    this.setData({
      regionArray: [],
      regionText: '请选择省市区',
      formData: {
        consignee: '',
        phone: '',
        province: '',
        city: '',
        district: '',
        detail: '',
        isDefault: 0
      }
    });
  },

  loadAddresses() {
    const api = require('../../api/index.js');
    api.getAddressList(this.data.userId).then(res => {
      this.setData({ addresses: res.data || [] });
    }).catch(err => {
      console.error('加载地址失败', err);
    });
  },

  loadAddressesForEdit(id) {
    const api = require('../../api/index.js');
    api.getAddressList(this.data.userId).then(res => {
      const addresses = res.data || [];
      this.setData({ addresses: addresses });
      const address = addresses.find(item => item.id === id);
      if (address) {
        this.setData({
          formData: {
            consignee: address.consignee || '',
            phone: address.phone || '',
            province: address.province || '',
            city: address.city || '',
            district: address.district || '',
            detail: address.detail || '',
            isDefault: address.isDefault || 0
          },
          regionArray: [address.province, address.city, address.district],
          regionText: address.province + ' ' + address.city + ' ' + address.district
        });
      }
    }).catch(err => {
      console.error('加载地址失败', err);
    });
  },

  onConsigneeInput(e) {
    this.setData({ 'formData.consignee': e.detail.value });
  },

  onPhoneInput(e) {
    this.setData({ 'formData.phone': e.detail.value });
  },

  onRegionChange(e) {
    const value = e.detail.value;
    const regionText = value.join(' ');
    this.setData({
      regionArray: value,
      regionText: regionText,
      'formData.province': value[0],
      'formData.city': value[1],
      'formData.district': value[2]
    });
  },

  onDetailInput(e) {
    this.setData({ 'formData.detail': e.detail.value });
  },

  onDefaultChange(e) {
    this.setData({ 'formData.isDefault': e.detail.value ? 1 : 0 });
  },

  onCancelAddress() {
    wx.navigateBack();
  },

  onSaveAddress() {
    const formData = this.data.formData;
    if (!formData.consignee || !formData.consignee.trim()) {
      wx.showToast({ title: '请输入收货人', icon: 'none' });
      return;
    }
    if (!formData.phone || !formData.phone.trim()) {
      wx.showToast({ title: '请输入手机号', icon: 'none' });
      return;
    }
    const phoneReg = /^1[3-9]\d{9}$/;
    if (!phoneReg.test(formData.phone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' });
      return;
    }
    if (!formData.province || !formData.province.trim()) {
      wx.showToast({ title: '请选择省市区', icon: 'none' });
      return;
    }
    if (!formData.detail || !formData.detail.trim()) {
      wx.showToast({ title: '请输入详细地址', icon: 'none' });
      return;
    }

    const api = require('../../api/index.js');
    const saveData = { ...formData };

    if (this.data.action === 'edit' && this.data.editingId) {
      saveData.id = this.data.editingId;
      api.updateAddress(this.data.userId, saveData).then(() => {
        wx.showToast({ title: '修改成功', icon: 'success' });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }).catch(err => {
        console.error('修改地址失败', err);
        wx.showToast({ title: '修改失败', icon: 'none' });
      });
    } else {
      api.addAddress(this.data.userId, saveData).then(() => {
        wx.showToast({ title: '添加成功', icon: 'success' });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }).catch(err => {
        console.error('添加地址失败', err);
        wx.showToast({ title: '添加失败', icon: 'none' });
      });
    }
  },

  onAddAddress() {
    wx.navigateTo({
      url: '/pages/address/address?action=add'
    });
  },

  onEditAddress(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/address/address?action=edit&id=${id}`
    });
  },

  onSelectAddress(e) {
    if (this.data.from === 'confirm') {
      const { id } = e.currentTarget.dataset;
      const api = require('../../api/index.js');
      api.setDefaultAddress(this.data.userId, id).then(() => {
        wx.navigateBack();
      }).catch(err => {
        console.error('设置默认地址失败', err);
      });
    }
  },

  onDeleteAddress(e) {
    const { id } = e.currentTarget.dataset;
    wx.showModal({
      title: '提示',
      content: '确定要删除该地址吗？',
      success: (res) => {
        if (res.confirm) {
          const api = require('../../api/index.js');
          api.deleteAddress(this.data.userId, id).then(() => {
            wx.showToast({ title: '删除成功', icon: 'success' });
            this.loadAddresses();
          }).catch(err => {
            console.error('删除地址失败', err);
          });
        }
      }
    });
  }
});
