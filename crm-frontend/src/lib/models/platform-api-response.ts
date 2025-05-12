export type PlatformApiResponse<T> = {
    status: string;
    message: string;
    data: T | null;
}

export function isPlatformApiResponse<T>(object: any): object is PlatformApiResponse<T> {
    return (
        typeof object.status === 'string' &&
        typeof object.message === 'string' &&
        (object.data === null || typeof object.data === 'object')
    );
}