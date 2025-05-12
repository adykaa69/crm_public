export type ErrorResponse = {
    message: string;
    errorCode: string;
    timestamp: string;
}

export function isErrorResponse(object: any): object is ErrorResponse {
    return (
        typeof object.message === 'string' &&
        typeof object.errorCode === 'string' &&
        typeof object.timestamp === 'string'
    );
}