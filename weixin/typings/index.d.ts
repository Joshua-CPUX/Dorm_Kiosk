/// <reference path="./types/index.d.ts" />

interface IAppOption {
  globalData: {
    userId: number | null;
    token: string | null;
    userInfo: any;
  }
  onLaunch?: () => void;
  onShow?: () => void;
  onHide?: () => void;
  onUnlaunch?: () => void;
  setUserData?: (token: string, userId: number, userInfo: any) => void;
  clearUserData?: () => void;
  userInfoReadyCallback?: WechatMiniprogram.GetUserInfoSuccessCallback;
}