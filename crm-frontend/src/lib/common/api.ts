export const baseApiUrl = "http://127.0.0.1:8080";

export function httpGet(path: string, token?: string) {
  return req(path, undefined, token);
}
export async function httpPost(path: string, data: any, header?: any) {
  return req(path, "POST", header, data);
}
export function httpPut(path: string, data: any, header?: any) {
  return req(path, "PUT", header, data);
}
export function httpPatch(path: string, data: any, header?: any) {
  return req(path, "PATCH", header, data);
}
export function httpDelete(path: string, header?: any) {
  return fetch(baseApiUrl + path, {
    method: "DELETE",
    mode: "cors"
  });
}

async function req(path: string, method = "GET", header?: any, data?: any) {
  return fetch(baseApiUrl + path, {
    method,
    headers: {
      "Content-Type": "application/json",
      Authorization: header
    },
    body: data && JSON.stringify(data)
  });
}
