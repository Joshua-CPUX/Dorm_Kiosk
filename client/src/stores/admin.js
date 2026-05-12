import { defineStore } from 'pinia';
import { adminLogin, getAdminInfo } from '../api/admin';

export const useAdminStore = defineStore('admin', {
  state: () => ({
    token: localStorage.getItem('admin_token') || '',
    adminId: localStorage.getItem('admin_id') || null,
    adminInfo: null
  }),

  actions: {
    async login(username, password) {
      const res = await adminLogin({ username, password });
      this.token = res.data.token;
      this.adminInfo = res.data;
      this.adminId = res.data.id;
      localStorage.setItem('admin_token', this.token);
      localStorage.setItem('admin_id', res.data.id);
      return res;
    },

    async getInfo() {
      if (!this.adminInfo && this.token) {
        const res = await getAdminInfo(this.adminId);
        this.adminInfo = res.data;
      }
      return this.adminInfo;
    },

    logout() {
      this.token = '';
      this.adminInfo = null;
      this.adminId = null;
      localStorage.removeItem('admin_token');
      localStorage.removeItem('admin_id');
    }
  }
});
