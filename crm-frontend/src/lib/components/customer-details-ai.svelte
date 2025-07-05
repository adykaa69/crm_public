<script lang="ts">
  // Customer data state
  let customerData = $state({
    firstName: "John",
    lastName: "Doe",
    nickname: "Johnny",
    email: "john.doe@example.com",
    phone: "+1 (555) 123-4567",
    relationship: "Premium Customer",
    address: {
      addressLine: "123 Main Street",
      addressLine2: "Apt 4B",
      city: "New York",
      zipcode: "10001",
      country: "United States"
    }
  });

  // Notes data
  let notes = $state([
    {
      id: 1,
      date: "2024-01-15",
      content: "Customer called regarding billing inquiry. Resolved payment issue and updated account information.",
      author: "Sarah Johnson"
    },
    {
      id: 2,
      date: "2024-01-10",
      content: "Follow-up call scheduled for next week. Customer interested in premium package upgrade.",
      author: "Mike Chen"
    },
    {
      id: 3,
      date: "2024-01-05",
      content: "Initial consultation completed. Customer requirements documented and proposal sent.",
      author: "Sarah Johnson"
    },
    {
      id: 4,
      date: "2023-12-28",
      content: "Customer onboarding process initiated. Welcome package sent via email.",
      author: "Alex Rodriguez"
    },
    {
      id: 5,
      date: "2023-12-20",
      content: "Account created successfully. Customer verification completed.",
      author: "Sarah Johnson"
    }
  ]);

  let newNote = $state("");
  let isEditing = $state(false);

  function addNote() {
    if (newNote.trim()) {
      notes.unshift({
        id: Date.now(),
        date: new Date().toISOString().split("T")[0],
        content: newNote.trim(),
        author: "Current User"
      });
      newNote = "";
    }
  }

  function toggleEdit() {
    isEditing = !isEditing;
  }

  function saveCustomer() {
    isEditing = false;
    // Here you would typically save to your backend
    console.log("Saving customer data:", customerData);
  }
</script>

<div class="min-h-screen bg-gray-50">
  <div class="container mx-auto p-6">
    <div class="mb-6">
      <h1 class="text-3xl font-bold text-gray-900">Customer Details</h1>
      <p class="mt-2 text-gray-600">Manage customer information and interaction history</p>
    </div>

    <div class="grid h-[calc(100vh-200px)] gap-6 lg:grid-cols-2">
      <!-- Left Side - Notes Section (Scrollable) -->
      <div class="flex flex-col">
        <div class="flex flex-1 flex-col rounded-lg border bg-white shadow-sm">
          <div class="flex-shrink-0 border-b p-6">
            <h2 class="flex items-center gap-2 text-xl font-semibold text-gray-900">
              <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                />
              </svg>
              Customer Notes
            </h2>
          </div>
          <div class="flex flex-1 flex-col gap-4 p-6">
            <!-- Add New Note -->
            <div class="space-y-3">
              <textarea
                bind:value={newNote}
                placeholder="Add a new note..."
                class="min-h-[80px] w-full resize-none rounded-md border border-gray-300 p-3 outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-500"
              ></textarea>
              <button
                onclick={addNote}
                disabled={!newNote.trim()}
                class="flex w-full items-center justify-center gap-2 rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-gray-300"
              >
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                </svg>
                Add Note
              </button>
            </div>

            <hr class="border-gray-200" />

            <!-- Notes List (Scrollable) -->
            <div class="flex-1 space-y-4 overflow-y-auto pr-2">
              {#each notes as note (note.id)}
                <div class="rounded-lg border bg-gray-50 p-4">
                  <div class="mb-2 flex items-start justify-between">
                    <span class="inline-block rounded-full bg-blue-100 px-2 py-1 text-xs text-blue-800">
                      {note.date}
                    </span>
                    <span class="text-xs text-gray-500">{note.author}</span>
                  </div>
                  <p class="text-sm leading-relaxed text-gray-700">{note.content}</p>
                </div>
              {/each}
            </div>
          </div>
        </div>
      </div>

      <!-- Right Side - Customer Information (Fixed) -->
      <div class="flex flex-col">
        <div class="flex-1 rounded-lg border bg-white shadow-sm">
          <div class="flex-shrink-0 border-b p-6">
            <div class="flex items-center justify-between">
              <h2 class="flex items-center gap-2 text-xl font-semibold text-gray-900">
                <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                  />
                </svg>
                Customer Information
              </h2>
              <button
                onclick={toggleEdit}
                class="rounded-md border border-gray-300 px-4 py-2 text-sm font-medium hover:bg-gray-50"
              >
                {isEditing ? "Cancel" : "Edit"}
              </button>
            </div>
          </div>
          <div class="overflow-y-auto p-6">
            <div class="space-y-6">
              <!-- Personal Information -->
              <div class="space-y-4">
                <h3 class="text-lg font-semibold text-gray-900">Personal Details</h3>
                <div class="grid grid-cols-2 gap-4">
                  <div class="space-y-2">
                    <label for="firstName" class="block text-sm font-medium text-gray-700">First Name</label>
                    <input
                      id="firstName"
                      type="text"
                      bind:value={customerData.firstName}
                      disabled={!isEditing}
                      class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                    />
                  </div>
                  <div class="space-y-2">
                    <label for="lastName" class="block text-sm font-medium text-gray-700">Last Name</label>
                    <input
                      id="lastName"
                      type="text"
                      bind:value={customerData.lastName}
                      disabled={!isEditing}
                      class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                    />
                  </div>
                </div>
                <div class="space-y-2">
                  <label for="nickname" class="block text-sm font-medium text-gray-700">Nickname</label>
                  <input
                    id="nickname"
                    type="text"
                    bind:value={customerData.nickname}
                    disabled={!isEditing}
                    class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                  />
                </div>
              </div>

              <hr class="border-gray-200" />

              <!-- Contact Information -->
              <div class="space-y-4">
                <h3 class="flex items-center gap-2 text-lg font-semibold text-gray-900">
                  <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M3 8l7.89 4.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
                    />
                  </svg>
                  Contact Information
                </h3>
                <div class="space-y-2">
                  <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                  <input
                    id="email"
                    type="email"
                    bind:value={customerData.email}
                    disabled={!isEditing}
                    class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                  />
                </div>
                <div class="space-y-2">
                  <label for="phone" class="block text-sm font-medium text-gray-700">Phone</label>
                  <input
                    id="phone"
                    type="tel"
                    bind:value={customerData.phone}
                    disabled={!isEditing}
                    class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                  />
                </div>
                <div class="space-y-2">
                  <label for="relationship" class="block text-sm font-medium text-gray-700">Relationship Status</label>
                  {#if isEditing}
                    <select
                      bind:value={customerData.relationship}
                      class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="New Customer">New Customer</option>
                      <option value="Regular Customer">Regular Customer</option>
                      <option value="Premium Customer">Premium Customer</option>
                      <option value="VIP Customer">VIP Customer</option>
                    </select>
                  {:else}
                    <div class="flex items-center gap-2">
                      <svg class="h-4 w-4 text-red-500" fill="currentColor" viewBox="0 0 24 24">
                        <path
                          d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                        />
                      </svg>
                      <span class="inline-block rounded-full bg-gray-100 px-3 py-1 text-sm text-gray-800">
                        {customerData.relationship}
                      </span>
                    </div>
                  {/if}
                </div>
              </div>

              <hr class="border-gray-200" />

              <!-- Address Information -->
              <div class="space-y-4">
                <h3 class="flex items-center gap-2 text-lg font-semibold text-gray-900">
                  <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                    />
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                    />
                  </svg>
                  Residence Information
                </h3>
                <div class="space-y-2">
                  <label for="addressLine" class="block text-sm font-medium text-gray-700">Address Line 1</label>
                  <input
                    id="addressLine"
                    type="text"
                    bind:value={customerData.address.addressLine}
                    disabled={!isEditing}
                    class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                  />
                </div>
                <div class="space-y-2">
                  <label for="addressLine2" class="block text-sm font-medium text-gray-700">Address Line 2</label>
                  <input
                    id="addressLine2"
                    type="text"
                    bind:value={customerData.address.addressLine2}
                    disabled={!isEditing}
                    class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                  />
                </div>
                <div class="grid grid-cols-2 gap-4">
                  <div class="space-y-2">
                    <label for="city" class="block text-sm font-medium text-gray-700">City</label>
                    <input
                      id="city"
                      type="text"
                      bind:value={customerData.address.city}
                      disabled={!isEditing}
                      class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                    />
                  </div>
                  <div class="space-y-2">
                    <label for="zipcode" class="block text-sm font-medium text-gray-700">Zip Code</label>
                    <input
                      id="zipcode"
                      type="text"
                      bind:value={customerData.address.zipcode}
                      disabled={!isEditing}
                      class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                    />
                  </div>
                </div>
                <div class="space-y-2">
                  <label for="country" class="block text-sm font-medium text-gray-700">Country</label>
                  <input
                    id="country"
                    type="text"
                    bind:value={customerData.address.country}
                    disabled={!isEditing}
                    class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50 disabled:text-gray-500"
                  />
                </div>
              </div>

              {#if isEditing}
                <div class="flex gap-2 pt-4">
                  <button
                    onclick={saveCustomer}
                    class="flex-1 rounded-md bg-blue-600 px-4 py-2 font-medium text-white hover:bg-blue-700"
                  >
                    Save Changes
                  </button>
                  <button
                    onclick={toggleEdit}
                    class="flex-1 rounded-md border border-gray-300 px-4 py-2 font-medium hover:bg-gray-50"
                  >
                    Cancel
                  </button>
                </div>
              {/if}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
