export interface TaskRequest {
  customerId: string | null;
  title: string;
  description: string;
  status: TaskStatus;
  reminder?: Date;
  dueDate?: Date;
}

export interface TaskUpdateRequest {
  id: string;
  customerId: string | null;
  title: string;
  description: string;
  status: TaskStatus;
  reminder?: Date;
  dueDate?: Date;
  completedAt?: Date;
  createdAt: Date;
  updatedAt: Date;
}

export interface TaskResponse {
  id: string;
  customerId: string | null;
  title: string;
  description: string;
  status: TaskStatus;
  reminder?: Date;
  dueDate?: Date;
  completedAt?: Date;
  createdAt: Date;
  updatedAt: Date;
}

export interface TaskDto {
  id: string;
  customerId?: string;
  title: string;
  description?: string;
  status: TaskStatus;
  reminder?: Date;
  dueDate?: Date;
  completedAt?: Date;
  createdAt: Date;
  updatedAt: Date;
}

// export enum TaskStatus {
//   OPEN = "Nyitott",
//   IN_PROGRESS = "Folyamatban",
//   ON_HOLD = "Várakozik",
//   BLOCKED = "Blokkolt",
//   PENDING = "pending",
//   COMPLETED = "Teljesítve",
//   CANCELLED = "Törölve",
//   ARCHIVED = "Archivált"
// }

export enum TaskStatus {
  OPEN = "OPEN",
  IN_PROGRESS = "IN_PROGRESS",
  ON_HOLD = "ON_HOLD",
  BLOCKED = "BLOCKED",
  PENDING = "PENDING",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
  ARCHIVED = "ARCHIVED"
}

export function parseTaskResponse(raw: any): TaskDto {
  return {
    id: raw.id,
    customerId: raw.customerId,
    title: raw.title,
    description: raw.description,
    status: raw.status,
    reminder: getDate(raw.reminder),
    dueDate: getDate(raw.dueDate),
    completedAt: getDate(raw.completedAt),
    createdAt: new Date(raw.createdAt),
    updatedAt: new Date(raw.updatedAt)
  };
}

function getDate(raw: any) {
  if (!raw) {
    return;
  }
  return new Date(raw);
}
