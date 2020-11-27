import axios from 'axios';
import { Toast } from 'vant';

export default {
  requestPatient ({ commit }, { openid }) {
    return new Promise((resolve, reject) => {
      const toast = Toast.loading({
        forbidClick: true,
        duration: 0
      });
      axios.get("/api/pmsPatient/patientByOpenId", {
        params: {
          openId: openid,
        },
      }).then(({ data: { data } }) => {
        if (data && data.id) {
          commit('setPatientId', data.id);
          resolve();
        } else {
          reject();
        }
      })
      .catch(resolve)
      .finally(() => {
        toast.clear();
      });
    });
  },
  // setUserInfo(state, info) {
  //   state.userInfo = info;
  // },
  // setLayout(state, type) {
  //   state.hideLayout = type;
  // }
}