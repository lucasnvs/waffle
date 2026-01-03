import http from 'k6/http';
import { check } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 50 },
    { duration: '20s', target: 200 },
    { duration: '10s', target: 0 },
  ],
};

const BASE_URL = 'http://localhost:8080';

export default function () {
  const raffleId = 1;

  const payload = JSON.stringify({
    number: 7,
    userId: 'same-user',
  });

  const headers = {
    'Content-Type': 'application/json',
  };

  const res = http.post(
    `${BASE_URL}/raffles/${raffleId}/tickets`,
    payload,
    { headers }
  );

  check(res, {
    'status is 202 or 409': (r) =>
      r.status === 202 || r.status === 409,
  });
}
