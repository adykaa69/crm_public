<script lang="ts">
  import { Calendar, Users, FileText, CheckSquare, Plus, Home, Clock, DollarSign, ListTodo } from "@lucide/svelte";

  let sidebarItems = [
    { icon: Home, label: "Dashboard", active: true, count: null },
    { icon: Clock, label: "Next Appointments", active: false, count: 4 },
    { icon: DollarSign, label: "Recent Deals", active: false, count: null },
    { icon: Users, label: "Customers", active: false, count: null },
    { icon: ListTodo, label: "Tasks To Do", active: false, count: null }
  ];

  let recentDeals = [
    { name: "Old Head Road", amount: "$37,500", avatar: "/placeholder.svg?height=32&width=32" },
    { name: "27 Spruce Drive", amount: "$37,500", avatar: "/placeholder.svg?height=32&width=32" },
    { name: "345 Belmont Drive", amount: "$37,500", avatar: "/placeholder.svg?height=32&width=32" },
    { name: "18-20 Harcourt Road", amount: "$37,500", avatar: "/placeholder.svg?height=32&width=32" },
    { name: "1824 Turkey Pen Road", amount: "$37,500", avatar: "/placeholder.svg?height=32&width=32" }
  ];

  let customers = [
    { name: "Dianne Ames", avatar: "/placeholder.svg?height=32&width=32" },
    { name: "Andrea White", avatar: "/placeholder.svg?height=32&width=32" },
    { name: "Rose Rodriguez", avatar: "/placeholder.svg?height=32&width=32" }
  ];

  let tasks = [
    { text: "Meeting with partners", date: "24 Nov 2023", completed: false },
    { text: "New customers agenda", date: "24 Nov 2023", completed: false },
    { text: "Lunch with Steve", date: "24 Nov 2023", completed: false },
    { text: "Meeting with partners", date: "24 Nov 2023", completed: false },
    { text: "Weekly meeting", date: "24 Nov 2023", completed: false },
    { text: "Add new services", date: "24 Nov 2023", completed: false }
  ];

  let customerCount = $state(78);
  let dealCount = $state(136);
</script>

<div class="flex h-screen bg-gray-50">
  <!-- Left Sidebar -->
  <div class="flex w-80 flex-col border-r border-gray-200 bg-white">
    <!-- Sidebar Header -->
    <div class="border-b border-gray-200 p-6">
      <div class="flex items-center space-x-2">
        <div class="flex h-8 w-8 items-center justify-center rounded-lg bg-blue-600">
          <span class="text-sm font-bold text-white">D</span>
        </div>
        <span class="font-semibold text-gray-900">Dashboard</span>
      </div>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 p-4">
      <ul class="space-y-2">
        {#each sidebarItems as item}
          <li>
            <button
              class="flex w-full items-center justify-between rounded-lg px-3 py-2 text-left hover:bg-gray-100 {item.active
                ? 'bg-blue-50 text-blue-600'
                : 'text-gray-700'}"
            >
              <div class="flex items-center space-x-3">
                <item.icon class="h-5 w-5" />
                <span class="font-medium">{item.label}</span>
              </div>
              {#if item.count}
                <span class="rounded-full bg-blue-100 px-2 py-1 text-xs font-medium text-blue-600">{item.count}</span>
              {/if}
            </button>
          </li>
        {/each}
      </ul>
    </nav>

    <!-- Recent Deals Section -->
    <div class="border-t border-gray-200 p-4">
      <div class="mb-4 flex items-center justify-between">
        <h3 class="font-semibold text-gray-900">Recent Deals</h3>
        <button class="text-sm text-blue-600 hover:underline">View All</button>
      </div>
      <div class="space-y-3">
        {#each recentDeals as deal}
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-3">
              <img src={deal.avatar || "/placeholder.svg"} alt="" class="h-8 w-8 rounded-full" />
              <span class="text-sm text-gray-700">{deal.name}</span>
            </div>
            <span class="text-sm font-medium text-gray-900">{deal.amount}</span>
          </div>
        {/each}
      </div>
      <button class="mt-4 w-full rounded-lg bg-blue-600 px-4 py-2 text-white transition-colors hover:bg-blue-700">
        See Detail
      </button>
    </div>

    <!-- Customers Section -->
    <div class="border-t border-gray-200 p-4">
      <div class="mb-4 flex items-center justify-between">
        <h3 class="font-semibold text-gray-900">Customers</h3>
        <div class="flex items-center space-x-2">
          <div class="flex h-8 w-8 items-center justify-center rounded-full bg-green-100">
            <span class="text-xs font-bold text-green-600">+</span>
          </div>
          <span class="text-2xl font-bold text-gray-900">{customerCount}</span>
        </div>
      </div>
      <div class="space-y-3">
        {#each customers as customer}
          <div class="flex items-center space-x-3">
            <img src={customer.avatar || "/placeholder.svg"} alt="" class="h-8 w-8 rounded-full" />
            <span class="text-sm text-gray-700">{customer.name}</span>
          </div>
        {/each}
      </div>
      <button class="mt-3 text-sm text-blue-600 hover:underline">Load More</button>
    </div>

    <!-- Stats -->
    <div class="border-t border-gray-200 p-4">
      <div class="flex items-center space-x-4">
        <div class="flex items-center space-x-2">
          <div class="flex h-8 w-8 items-center justify-center rounded-full bg-red-100">
            <span class="text-xs font-bold text-red-600">-</span>
          </div>
          <span class="text-2xl font-bold text-gray-900">{dealCount}</span>
        </div>
      </div>
    </div>
  </div>

  <!-- Main Content -->
  <div class="flex flex-1 flex-col">
    <!-- Header -->
    <header class="border-b border-gray-200 bg-white px-6 py-4">
      <div class="flex items-center justify-between">
        <div class="flex items-center space-x-4">
          <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
        </div>
        <div class="flex items-center space-x-4">
          <button
            class="flex items-center space-x-2 rounded-lg bg-blue-600 px-4 py-2 text-white transition-colors hover:bg-blue-700"
          >
            <Plus class="h-4 w-4" />
            <span>Add new</span>
          </button>
          <div class="h-8 w-8 rounded-full bg-gray-300"></div>
        </div>
      </div>
    </header>

    <!-- Main Dashboard Content -->
    <main class="flex-1 p-6">
      <div class="grid h-full grid-cols-1 gap-6 lg:grid-cols-2">
        <!-- Left Column -->
        <div class="space-y-6">
          <!-- No Upcoming Appointments Card -->
          <div class="rounded-xl bg-gradient-to-br from-blue-500 to-purple-600 p-8 text-center text-white">
            <div class="mb-6">
              <Calendar class="mx-auto mb-4 h-12 w-12 opacity-80" />
              <h2 class="mb-2 text-xl font-semibold">No upcoming appointments.</h2>
            </div>
            <button class="rounded-lg bg-white px-6 py-3 font-medium text-blue-600 transition-colors hover:bg-gray-50">
              Add Deal?
            </button>
          </div>

          <!-- Customers Section -->
          <div class="rounded-xl bg-white p-6 text-center">
            <h3 class="mb-4 text-lg font-semibold text-gray-900">Customers</h3>
            <div class="mb-4">
              <div class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-gray-200">
                <Users class="h-8 w-8 text-gray-400" />
              </div>
              <div class="mb-2 text-4xl font-bold text-gray-900">0</div>
            </div>
          </div>

          <!-- Deals Section -->
          <div class="rounded-xl bg-white p-6 text-center">
            <h3 class="mb-4 text-lg font-semibold text-gray-900">Deals</h3>
            <div class="mb-4">
              <div class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-gray-200">
                <FileText class="h-8 w-8 text-gray-400" />
              </div>
              <div class="mb-2 text-4xl font-bold text-gray-900">0</div>
            </div>
          </div>
        </div>

        <!-- Right Column - Tasks -->
        <div class="rounded-xl bg-white p-6">
          <div class="mb-6 flex items-center justify-between">
            <h3 class="text-lg font-semibold text-gray-900">Tasks To Do</h3>
            <button class="text-sm text-blue-600 hover:underline">View All</button>
          </div>
          <div class="space-y-4">
            {#each tasks as task}
              <div class="flex items-start space-x-3 rounded-lg p-3 hover:bg-gray-50">
                <div class="mt-1 h-4 w-4 rounded border-2 border-gray-300"></div>
                <div class="flex-1">
                  <p class="text-sm text-gray-900">{task.text}</p>
                  <p class="mt-1 text-xs text-gray-500">{task.date}</p>
                </div>
              </div>
            {/each}
          </div>
          <div class="mt-6 border-t border-gray-200 pt-4">
            <button class="flex items-center space-x-2 text-sm text-blue-600 hover:underline">
              <Plus class="h-4 w-4" />
              <span>Add new task</span>
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>

  <!-- Right Sidebar -->
  <div class="w-80 space-y-6 border-l border-gray-200 bg-white p-6">
    <!-- No Deals Found -->
    <div class="rounded-xl bg-gray-50 p-6 text-center">
      <FileText class="mx-auto mb-3 h-8 w-8 text-gray-400" />
      <p class="text-sm text-gray-600">No deals found.</p>
    </div>

    <!-- No Customers Found -->
    <div class="rounded-xl bg-gray-50 p-6 text-center">
      <Users class="mx-auto mb-3 h-8 w-8 text-gray-400" />
      <p class="text-sm text-gray-600">No customers found.</p>
    </div>

    <!-- No Upcoming Tasks -->
    <div class="rounded-xl bg-gray-50 p-6 text-center">
      <CheckSquare class="mx-auto mb-3 h-8 w-8 text-gray-400" />
      <p class="text-sm text-gray-600">No upcoming tasks found.</p>
    </div>

    <!-- No Deals in Progress -->
    <div class="rounded-xl bg-gray-50 p-6 text-center">
      <FileText class="mx-auto mb-3 h-8 w-8 text-gray-400" />
      <p class="text-sm text-gray-600">No deals in progress.</p>
    </div>

    <!-- No Activity Logged -->
    <div class="rounded-xl bg-gray-50 p-6 text-center">
      <div class="mx-auto mb-3 flex h-8 w-8 items-center justify-center rounded-full bg-gray-300">
        <span class="text-xs text-gray-500">📊</span>
      </div>
      <p class="text-sm text-gray-600">No activity logged.</p>
    </div>
  </div>
</div>
