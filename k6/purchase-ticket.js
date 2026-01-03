import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 20,
  duration: '30s',
};

const BASE_URL = 'http://localhost:8080';

export default function () {
  const payload = JSON.stringify({
    number: 7
  });

  const headers = {
    'Content-Type': 'application/json',
    'X-User-Id': 'rate-limit-user'
  };

  const res = http.post(
    `${BASE_URL}/raffles/1/tickets`,
    payload,
    { headers }
  );

  check(res, {
    'status is 202, 409 or 429': (r) =>
      r.status === 202 ||
      r.status === 409 ||
      r.status === 429,
  });
}
