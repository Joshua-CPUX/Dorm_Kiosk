import { defineStore } from 'pinia';
import { adminLogin, getAdminInfo } from '../api/admin';

export const useAdminStore = defineStore('admin', {
  state: () => ({
    token: localStorage.getItem('admin_token') || '',
    adminInfo: null
  }),

  actions: {
    async login(username, password) {
      const res = await adminLogin({ username, password });
      this.token = res.data.token;
      this.adminInfo = res.data;
      localStorage.setItem('admin_token', this.token);
      return res;
    },

    async getInfo() {
      if (!this.adminInfo && this.token) {
        const res = await getAdminInfo(this.adminInfo?.id);
        this.adminInfo = res.data;
      }
      return this.adminInfo;
    },

    logout() {
      this.token = '';
      this.adminInfo = null;
      localStorage.removeItem('admin_token');
    }
  }
});
