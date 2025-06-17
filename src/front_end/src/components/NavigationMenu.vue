<template>
    <div class="ml-4">
        <h2 class="text-2xl font-bold text-gray-900">RA</h2>
        <div class="relative inline-block text-left mt-2">
            <select v-model="currentOption" @change="handleChange" class="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50 py-1.5 pl-1 pr-1 text-base">
                <option value="chat">
                    <div class="flex flex-col items-start">
                        <span class="font-medium text-gray-900">你的智能学术助手</span>
                    </div>
                </option>
                <option value="Thesis">
                    <div class="flex items-center">
                        <span class="font-medium">你的论文分析助手</span>
                    </div>
                </option>
                <option value="Literature">
                    <div class="flex items-center">
                        <span class="font-medium">你的文献分析助手</span>
                    </div>
                </option>
            </select>
        </div>
    </div>
</template>

<script setup>
import {ref, watch , defineProps} from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps({
    selectedOption: {
        type: String,
        default: 'chat' // 默认选中项
    }
});

const router = useRouter();
const currentOption = ref(props.selectedOption); // 双向绑定到props

// 监听props变化，更新选中状态
watch(() => props.selectedOption, (newValue) => {
    currentOption.value = newValue;
});

// 处理菜单选择变化
const handleChange = () => {
    router.push(`/${currentOption.value}`);
};

</script>
