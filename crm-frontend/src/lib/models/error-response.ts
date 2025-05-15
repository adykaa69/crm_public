export type ErrorResponse = {
  errorCode: string;
  errorMessage: string;
  timestamp: string;
};

export function isErrorResponse(object: any): object is ErrorResponse {
  return (
    typeof object.errorMessage === "string" &&
    typeof object.errorCode === "string" &&
    typeof object.timestamp === "string"
  );
}
