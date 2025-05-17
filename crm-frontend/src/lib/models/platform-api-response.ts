export interface PlatformApiResponse<T> {
  status: string;
  message: string;
  data: T | null;
}
