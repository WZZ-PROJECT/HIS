import router from '@/router';
import store from '@/store'

router.beforeEach(async (to, from, next) => {
  const patientId = store.state.patientId;
  const openid = localStorage.openID;
  if (!patientId && openid) {
    try {
      await store.dispatch('requestPatient', {
        openid
      });
    } catch(e) {}
  }
  next();
})