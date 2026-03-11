import { financeApp } from './financeApp'

export const applications = [financeApp]
export const applicationsMap = Object.fromEntries(applications.map((a) => [a.id, a]))
