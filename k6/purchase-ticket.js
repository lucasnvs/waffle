import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 20,
  duration: '30s',
};

const BASE_URL = 'http://localhost:8080';

export default function () {
  const payload = JSON.stringify({
    number: 900,
  });

  const headers = {
    'Content-Type': 'application/json',
    'X-User-Id': 'rate-limit-user',
    'Idempotency-Key': `${__VU}-${__ITER}`,
  };

  const res = http.post(
    `${BASE_URL}/raffles/1/tickets`,
    payload,
    { headers }
  );

  check(res, {
    'status is 202, 409, 429 or 503': (r) =>
      r.status === 202 ||
      r.status === 409 ||
      r.status === 429 ||
      r.status === 503,

    'no 500 errors': (r) => r.status !== 500,
  });

  sleep(0.1);
}
