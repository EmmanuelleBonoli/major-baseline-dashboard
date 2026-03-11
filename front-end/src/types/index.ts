export enum Platform {
  ANDROID = 'ANDROID',
  IOS = 'IOS'
}

export enum MetricType {
  DOWNLOADS = 'DOWNLOADS',
  INSTALLATIONS = 'INSTALLATIONS',
  ACTIVE_USERS = 'ACTIVE_USERS',
  SESSIONS = 'SESSIONS',
  REVENUE = 'REVENUE',
  IN_APP_PURCHASES = 'IN_APP_PURCHASES',
  RETENTION_DAY_1 = 'RETENTION_DAY_1',
  RETENTION_DAY_7 = 'RETENTION_DAY_7',
  RETENTION_DAY_30 = 'RETENTION_DAY_30',
  CRASHES = 'CRASHES',
  RATINGS = 'RATINGS',
  REVIEWS = 'REVIEWS'
}

export interface Store {
  id: string
  name: string
  packageName?: string
  bundleId?: string
  platform: Platform
  icon?: string
  applicationId?: string
}

export interface Application {
  id: string
  name: string
  icon?: string
  studio?: string
  stores: Store[]
}

export interface Stats {
  date: string
  metricType: MetricType
  value: number
  revenueAmount?: number
  currency?: string
}

export interface Summary {
  totalDownloads: number
  activeUsers: number
  totalRevenue: number
  currency: string
  avgRating?: number
  totalReviews?: number
}

export interface DataPoint {
  date: string
  value: number
}

export interface ChartData {
  label: string
  data: DataPoint[]
}

export interface AppDashboard {
  store: Store
  summary: Summary
  charts: ChartData[]
}

export interface DateRange {
  startDate: string
  endDate: string
}
