export default {
  setLoginState (state, flag) {
    state.loginState = flag
  },
  setUserInfo(state, info) {
    state.userInfo = info;
  },
  setLayout(state, type) {
    state.hideLayout = type;
  },
  setPatientId(state, id) {
    state.patientId = id;
  },
  removePatientId(state) {
    state.patientId = '';
  }
}