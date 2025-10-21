<script lang="ts">
  import { Calendar, Users, DollarSign, Clock, ArrowRight, MoreHorizontal, X, Menu } from "@lucide/svelte";

  // Sample data using Svelte 5 runes
  let nextAppointment = $state({
    address: "319 Haul Road",
    location: "Glenrock, WY 12345",
    date: "Nov 18 2021, 17:00",
    roomArea: "100 M²",
    people: 10,
    price: "$5750"
  });

  let recentDeals = $state([
    {
      id: 1,
      address: "319 Haul Road",
      location: "Glenrock, WY",
      price: "$5750",
      date: "Nov 14, 07:00 AM",
      image: "/placeholder.svg?height=40&width=40"
    },
    {
      id: 2,
      address: "47 Spruce Drive",
      location: "Quantico, VA",
      price: "$5750",
      date: "Nov 15, 08:00 AM",
      image: "/placeholder.svg?height=40&width=40"
    },
    {
      id: 3,
      address: "165 Belmont Drive",
      location: "Parowan, UT",
      price: "$5750",
      date: "Nov 16, 09:00 AM",
      image: "/placeholder.svg?height=40&width=40"
    },
    {
      id: 4,
      address: "1538 Hammer Road",
      location: "Cleveland, OH",
      price: "$5750",
      date: "Nov 17, 10:00 AM",
      image: "/placeholder.svg?height=40&width=40"
    }
  ]);

  let customers = $state([
    {
      id: 1,
      name: "Deanna Annis",
      email: "deannannis@gmail.com",
      avatar: "/placeholder.svg?height=40&width=40"
    },
    {
      id: 2,
      name: "Andrea Willis",
      email: "andreawillis@gmail.com",
      avatar: "/placeholder.svg?height=40&width=40"
    },
    {
      id: 3,
      name: "Brent Rodrigues",
      email: "brodrigues@gmail.com",
      avatar: "/placeholder.svg?height=40&width=40"
    }
  ]);

  let tasks = $state([
    {
      id: 1,
      date: "30 Nov 2021",
      task: "Meeting with partners",
      urgent: true
    },
    {
      id: 2,
      date: "24 Dec 2021",
      task: "Web conference agenda",
      urgent: true
    },
    {
      id: 3,
      date: "24 Oct 2022",
      task: "Lunch with Steve",
      urgent: false
    },
    {
      id: 4,
      date: "24 Nov 2022",
      task: "Meeting with partners",
      urgent: false
    },
    {
      id: 5,
      date: "24 Nov 2022",
      task: "Weekly meeting",
      urgent: false
    },
    {
      id: 6,
      date: "24 Nov 2022",
      task: "Add new services",
      urgent: false
    }
  ]);

  let activities = $state([
    {
      id: 1,
      address: "1824 Turkey Pen Road",
      location: "Cleveland, OH 12345",
      status: "IN PROGRESS",
      image: "/placeholder.svg?height=40&width=40"
    },
    {
      id: 2,
      date: "17 Nov 2021",
      description: "Installation of the new air conditioning system"
    },
    {
      id: 3,
      date: "17 Nov 2021",
      description: "Installation of the new air conditioning system"
    }
  ]);

  let stats = $state({
    customers: 78,
    deals: 136
  });

  let newTask = $state("");
  let isMobileMenuOpen = $state(false);

  function addTask() {
    if (newTask.trim()) {
      tasks.push({
        id: tasks.length + 1,
        date: new Date().toLocaleDateString("en-US", {
          day: "2-digit",
          month: "short",
          year: "numeric"
        }),
        task: newTask,
        urgent: false
      });
      newTask = "";
    }
  }

  function toggleMobileMenu() {
    isMobileMenuOpen = !isMobileMenuOpen;
  }
</script>

<div class="min-h-screen bg-gray-50">
  <!-- Mobile Header -->
  <div class="flex items-center justify-between bg-white p-4 shadow-sm lg:hidden">
    <h1 class="text-xl font-bold text-gray-900">Dashboard</h1>
    <button onclick={toggleMobileMenu} class="p-2">
      {#if isMobileMenuOpen}
        <X class="h-6 w-6" />
      {:else}
        <Menu class="h-6 w-6" />
      {/if}
    </button>
  </div>

  <div class="p-3 sm:p-4 lg:p-6">
    <div class="mx-auto max-w-7xl">
      <!-- Mobile: Single column stack -->
      <!-- Tablet: 2 columns -->
      <!-- Desktop: 3 columns -->
      <div class="grid grid-cols-1 gap-3 sm:gap-4 md:grid-cols-2 lg:gap-6 xl:grid-cols-3">
        <!-- Column 1: Next Appointment & Stats -->
        <div class="space-y-3 sm:space-y-4 md:col-span-2 lg:space-y-6 xl:col-span-1">
          <!-- Next Appointment Card -->
          <div
            class="rounded-xl bg-gradient-to-br from-blue-500 to-purple-600 p-4 text-white sm:p-5 lg:rounded-2xl lg:p-6"
          >
            <div class="mb-4 flex items-center gap-2 lg:mb-6">
              <div class="h-2 w-2 rounded-full bg-green-400"></div>
              <h2 class="text-base font-semibold sm:text-lg">Next Appointment</h2>
            </div>

            <div class="mb-4 flex items-center gap-3 lg:mb-6">
              <div
                class="flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-full bg-white/20 sm:h-12 sm:w-12"
              >
                <Calendar class="h-5 w-5 sm:h-6 sm:w-6" />
              </div>
              <div class="min-w-0 flex-1">
                <div class="truncate text-base font-semibold sm:text-lg">{nextAppointment.address}</div>
                <div class="truncate text-sm text-white/80">{nextAppointment.location}</div>
              </div>
            </div>

            <div class="mb-4 lg:mb-6">
              <div class="mb-1 text-sm text-white/80">Appointment Date</div>
              <div class="text-base font-semibold sm:text-lg">{nextAppointment.date}</div>
            </div>

            <div class="mb-4 flex justify-between gap-4 lg:mb-6">
              <div class="flex-1">
                <div class="mb-1 text-sm text-white/80">Room Area</div>
                <div class="text-lg font-semibold sm:text-xl">{nextAppointment.roomArea}</div>
              </div>
              <div class="flex-1">
                <div class="mb-1 text-sm text-white/80">People</div>
                <div class="text-lg font-semibold sm:text-xl">{nextAppointment.people}</div>
              </div>
            </div>

            <div class="flex flex-col items-start justify-between gap-3 sm:flex-row sm:items-center">
              <div>
                <div class="mb-1 text-sm text-white/80">Price</div>
                <div class="text-xl font-bold sm:text-2xl">{nextAppointment.price}</div>
              </div>
              <button
                class="w-full rounded-full bg-white px-4 py-2 text-sm font-semibold text-purple-600 transition-colors hover:bg-gray-100 sm:w-auto sm:px-6 sm:text-base"
              >
                See Detail
              </button>
            </div>
          </div>

          <!-- Stats Cards - Responsive layout -->
          <div class="grid grid-cols-2 gap-3 sm:gap-4">
            <div class="rounded-lg bg-white p-4 shadow-sm sm:p-5 lg:rounded-xl lg:p-6">
              <div class="flex flex-col items-start justify-between gap-3 sm:flex-row sm:items-center">
                <div>
                  <div class="mb-1 text-xs text-gray-500 sm:text-sm">Customers</div>
                  <div class="text-2xl font-bold text-gray-900 sm:text-3xl lg:text-4xl">{stats.customers}</div>
                </div>
                <div
                  class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-green-100 sm:h-10 sm:w-10 lg:h-12 lg:w-12"
                >
                  <Users class="h-4 w-4 text-green-600 sm:h-5 sm:w-5 lg:h-6 lg:w-6" />
                </div>
              </div>
            </div>

            <div class="rounded-lg bg-white p-4 shadow-sm sm:p-5 lg:rounded-xl lg:p-6">
              <div class="flex flex-col items-start justify-between gap-3 sm:flex-row sm:items-center">
                <div>
                  <div class="mb-1 text-xs text-gray-500 sm:text-sm">Deals</div>
                  <div class="text-2xl font-bold text-gray-900 sm:text-3xl lg:text-4xl">{stats.deals}</div>
                </div>
                <div
                  class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-red-100 sm:h-10 sm:w-10 lg:h-12 lg:w-12"
                >
                  <DollarSign class="h-4 w-4 text-red-600 sm:h-5 sm:w-5 lg:h-6 lg:w-6" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Column 2: Recent Deals & Activities -->
        <div class="space-y-3 sm:space-y-4 lg:space-y-6">
          <!-- Recent Deals -->
          <div class="rounded-lg bg-white p-4 shadow-sm sm:p-5 lg:rounded-xl lg:p-6">
            <div class="mb-4 flex items-center justify-between lg:mb-6">
              <h3 class="text-lg font-semibold text-gray-900 sm:text-xl">Recent Deals</h3>
              <button class="text-sm font-medium text-blue-600 hover:text-blue-700 sm:text-base">View All</button>
            </div>

            <div class="space-y-2 sm:space-y-3 lg:space-y-4">
              {#each recentDeals.slice(0, 4) as deal}
                <div class="flex items-center gap-3 rounded-lg p-2 transition-colors hover:bg-gray-50 sm:gap-4 sm:p-3">
                  <img
                    src={deal.image || "/placeholder.svg"}
                    alt="Property"
                    class="h-8 w-8 flex-shrink-0 rounded-lg object-cover sm:h-10 sm:w-10 lg:h-12 lg:w-12"
                  />
                  <div class="min-w-0 flex-1">
                    <div class="truncate text-sm font-semibold text-gray-900 sm:text-base">{deal.address}</div>
                    <div class="truncate text-xs text-gray-500 sm:text-sm">{deal.location}</div>
                  </div>
                  <div class="flex-shrink-0 text-right">
                    <div class="text-sm font-semibold text-gray-900 sm:text-base">{deal.price}</div>
                    <div class="hidden text-xs text-gray-500 sm:block sm:text-sm">{deal.date}</div>
                  </div>
                </div>
              {/each}
            </div>
          </div>

          <!-- Activity Timeline - Hidden on mobile, shown on tablet+ -->
          <div class="hidden rounded-lg bg-white p-4 shadow-sm sm:p-5 md:block lg:rounded-xl lg:p-6">
            <div class="space-y-3 sm:space-y-4">
              {#each activities as activity}
                {#if activity.status}
                  <div class="flex items-center gap-3 rounded-lg border border-blue-200 bg-blue-50 p-3 sm:gap-4 sm:p-4">
                    <img
                      src={activity.image || "/placeholder.svg"}
                      alt="Property"
                      class="h-8 w-8 flex-shrink-0 rounded-lg object-cover sm:h-10 sm:w-10 lg:h-12 lg:w-12"
                    />
                    <div class="min-w-0 flex-1">
                      <div class="truncate text-sm font-semibold text-gray-900 sm:text-base">{activity.address}</div>
                      <div class="truncate text-xs text-gray-500 sm:text-sm">{activity.location}</div>
                    </div>
                    <div class="flex flex-shrink-0 items-center gap-2">
                      <span class="rounded-full bg-blue-600 px-2 py-1 text-xs font-medium text-white sm:px-3">
                        {activity.status}
                      </span>
                      <ArrowRight class="h-4 w-4 text-blue-600 sm:h-5 sm:w-5" />
                    </div>
                  </div>
                {:else}
                  <div class="flex items-start gap-3 p-2 sm:gap-4 sm:p-3">
                    <div
                      class="mt-1 flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-blue-600 sm:h-8 sm:w-8"
                    >
                      <div class="h-1.5 w-1.5 rounded-full bg-white sm:h-2 sm:w-2"></div>
                    </div>
                    <div class="min-w-0 flex-1">
                      <div class="mb-1 text-xs text-gray-500 sm:text-sm">{activity.date}</div>
                      <div class="text-sm text-gray-900 sm:text-base">{activity.description}</div>
                    </div>
                  </div>
                {/if}
              {/each}

              <button class="w-full py-2 text-sm font-medium text-blue-600 hover:text-blue-700 sm:text-base">
                Load More
              </button>
            </div>
          </div>
        </div>

        <!-- Column 3: Customers & Tasks -->
        <div class="space-y-3 sm:space-y-4 md:col-span-2 lg:space-y-6 xl:col-span-1">
          <!-- Customers -->
          <div class="rounded-lg bg-white p-4 shadow-sm sm:p-5 lg:rounded-xl lg:p-6">
            <div class="mb-4 flex items-center justify-between lg:mb-6">
              <h3 class="text-lg font-semibold text-gray-900 sm:text-xl">Customers</h3>
              <button class="text-sm font-medium text-blue-600 hover:text-blue-700 sm:text-base">View All</button>
            </div>

            <div class="space-y-2 sm:space-y-3 lg:space-y-4">
              {#each customers as customer}
                <div class="flex items-center gap-3 rounded-lg p-2 transition-colors hover:bg-gray-50">
                  <img
                    src={customer.avatar || "/placeholder.svg"}
                    alt={customer.name}
                    class="h-8 w-8 flex-shrink-0 rounded-full object-cover sm:h-10 sm:w-10"
                  />
                  <div class="min-w-0 flex-1">
                    <div class="truncate text-sm font-semibold text-gray-900 sm:text-base">{customer.name}</div>
                    <div class="truncate text-xs text-gray-500 sm:text-sm">{customer.email}</div>
                  </div>
                  <button class="flex-shrink-0 text-gray-400 hover:text-gray-600">
                    <MoreHorizontal class="h-4 w-4 sm:h-5 sm:w-5" />
                  </button>
                </div>
              {/each}
            </div>
          </div>

          <!-- Tasks To Do -->
          <div class="rounded-lg bg-white p-4 shadow-sm sm:p-5 lg:rounded-xl lg:p-6">
            <div class="mb-4 flex items-center justify-between lg:mb-6">
              <h3 class="text-lg font-semibold text-gray-900 sm:text-xl">Tasks To Do</h3>
              <button class="text-sm font-medium text-blue-600 hover:text-blue-700 sm:text-base">View All</button>
            </div>

            <div class="mb-4 space-y-2 sm:space-y-3 lg:mb-6">
              {#each tasks.slice(0, 6) as task}
                <div class="flex items-center gap-2 p-2 sm:gap-3">
                  <div class="w-16 flex-shrink-0 text-xs text-gray-500 sm:w-20 sm:text-sm">{task.date}</div>
                  {#if task.urgent}
                    <div class="h-1.5 w-1.5 flex-shrink-0 rounded-full bg-red-500 sm:h-2 sm:w-2"></div>
                  {/if}
                  <div class="flex-1 truncate text-sm text-gray-900 sm:text-base">{task.task}</div>
                </div>
              {/each}
            </div>

            <div class="flex items-center gap-2 border-t border-gray-200 pt-3 sm:pt-4">
              <input
                bind:value={newTask}
                placeholder="Add new task"
                class="flex-1 bg-transparent text-sm text-gray-500 outline-none sm:text-base"
                onkeydown={(e) => e.key === "Enter" && addTask()}
              />
              <button onclick={addTask} class="flex-shrink-0 text-blue-600 hover:text-blue-700">
                <ArrowRight class="h-4 w-4 sm:h-5 sm:w-5" />
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Mobile Activity Timeline (shown only on mobile) -->
      <div class="mt-3 sm:mt-4 md:hidden">
        <div class="rounded-lg bg-white p-4 shadow-sm">
          <h3 class="mb-4 text-lg font-semibold text-gray-900">Recent Activity</h3>
          <div class="space-y-3">
            {#each activities.slice(0, 2) as activity}
              {#if activity.status}
                <div class="flex items-center gap-3 rounded-lg border border-blue-200 bg-blue-50 p-3">
                  <img
                    src={activity.image || "/placeholder.svg"}
                    alt="Property"
                    class="h-8 w-8 flex-shrink-0 rounded-lg object-cover"
                  />
                  <div class="min-w-0 flex-1">
                    <div class="truncate text-sm font-semibold text-gray-900">{activity.address}</div>
                    <div class="truncate text-xs text-gray-500">{activity.location}</div>
                  </div>
                  <span class="flex-shrink-0 rounded-full bg-blue-600 px-2 py-1 text-xs font-medium text-white">
                    {activity.status}
                  </span>
                </div>
              {:else}
                <div class="flex items-start gap-3 p-2">
                  <div class="mt-1 flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-blue-600">
                    <div class="h-1.5 w-1.5 rounded-full bg-white"></div>
                  </div>
                  <div class="min-w-0 flex-1">
                    <div class="mb-1 text-xs text-gray-500">{activity.date}</div>
                    <div class="text-sm text-gray-900">{activity.description}</div>
                  </div>
                </div>
              {/if}
            {/each}
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
