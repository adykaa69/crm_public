export interface ErrorResponse {
  errorCode: string;
  errorMessage: string;
  timestamp: string;
}

export interface ErrorDto {
  errorCode: string;
  errorMessage: string;
  timestamp: Date;
}

export function toErrorDto(errorResponse: ErrorResponse): ErrorDto {
  return {
    errorCode: errorResponse.errorCode,
    errorMessage: errorResponse.errorMessage,
    timestamp: new Date(errorResponse.timestamp)
  };
}
