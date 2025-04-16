export const baseApiUrl = 'http://localhost:8080'

export function httpGet(path: string, token?: string) {
    return req(path, undefined, token);
}
export async function httpPost(path: string, data: any, header?: any) {
    return req(path, 'POST', header, data);
}
export function httpPut(path: string, data: any, header?: any) {
    return req(path, 'PUT', header, data);
}
export function httpPatch(path: string, data: any, header?: any) {
    return req(path, 'PATCH', header, data);
}
export function httpDelete(path: string, header?: any) {
    const res = fetch(baseApiUrl + path, {
        method: 'DELETE',
        mode: 'cors',
    })
    return res;
}

async function req(path: string, method = 'GET', header?: any, data?: any) {

    const res = fetch(baseApiUrl + path, {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': header
        },
        body: data && JSON.stringify(data)
    })
    return res;
}
