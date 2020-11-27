export function compareTime(h, m, now) {
  return new Date().setHours(h, m) >= now;
}

export function randomString(t) {
  t = t || 32;
  for (
    var e = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678",
      a = e.length,
      n = "",
      i = 0;
    i < t;
    i++
  )
    n += e.charAt(Math.floor(Math.random() * a));
  return n;
}