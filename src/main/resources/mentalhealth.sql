/*
 Navicat Premium Data Transfer

 Source Server         : data
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : mentalhealth

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 31/12/2024 01:30:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for doc_information
-- ----------------------------
DROP TABLE IF EXISTS `doc_information`;
CREATE TABLE `doc_information`  (
  `doc_id` int(11) NOT NULL AUTO_INCREMENT,
  `doc_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `doc_specialties` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `doc_profile` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `doc_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `doc_hospital` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `hos_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `spec_symptom` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`doc_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of doc_information
-- ----------------------------
INSERT INTO `doc_information` VALUES (1, '段艳平', '心理医学科', '心理医学科主治医师。', 'doctors/doctor1.jpg', '北京协和医院', 'https://www.pumch.cn/department_ims/doctor/detail/14143.html', '主治医师', '综合医院常见精神心理障碍的诊治，包括：抑郁障碍、广泛性焦虑障碍和惊恐障碍的诊断，以及相关药物治疗、团体治疗和短程整合心理治疗。擅长围产期心理健康的识别和产后抑郁的诊疗。');
INSERT INTO `doc_information` VALUES (2, '洪霞', '心理医学科', '洪霞，女，主任医师，教授，研究生导师，临床流行病学教研室主任。1993年从医，擅长综合医院各类精神心理问题的诊治，对老年期各类精神心理障碍、内分泌疾病合并的各类精神心理障碍有丰富的临床经验。系统学习和实践短程整合心理治疗，具有系统开展个体和团体心理治疗的经验。', 'doctors/doctor2.jpg', '北京协和医院', 'https://www.pumch.cn/department_ims/doctor/detail/4517.html', '临床流行病学教研室主任 主任医师 ', '综合医院各类精神心理问题的诊治，对老年期各类精神心理障碍、内分泌疾病合并的各类精神心理障碍有丰富的临床经验。系统学习和实践短程整合心理治疗，具有系统开展个体和团体心理治疗的经验。 ');
INSERT INTO `doc_information` VALUES (3, '史丽丽', '心理医学科', '史丽丽，女，2000年毕业于北京协和医学院，获得医学博士学位，从事心理医学专业。长期与消化科合作提供心身医学服务，研究功能性胃肠病和精神心理问题共病。系统学习和实践短程整合心理治疗，具有系统开展个体和团体心理治疗的经验。现为副主任医师。', 'doctors/doctor3.jpg', '北京协和医院', 'https://www.pumch.cn/department_ims/doctor/detail/4516.html', '副主任医师 ', '综合医院常见精神心理障碍及问题，如：抑郁、焦虑、功能性胃肠病的心理生理问题。协和星原计划带状疱疹多学科全病程管理团队成员。');
INSERT INTO `doc_information` VALUES (4, '李涛', '心理医学科', '医学博士，2007年毕业于北京协和医学院。北京医学会心身医学分会青年委员。', 'doctors/doctor4.jpg', '北京协和医院', 'https://www.pumch.cn/department_ims/doctor/detail/4511.html', '心理医学科主任助理 副主任医师', '功能性躯体症状、焦虑障碍、抑郁等综合医院常见精神心理障碍的药物及心理治疗。');
INSERT INTO `doc_information` VALUES (5, '姜忆南', '心理医学科', '男，北京协和医学院临床医学专业，医学博士，副主任医师', 'doctors/doctor5.jpg', '北京协和医院', 'https://www.pumch.cn/department_ims/doctor/detail/4510.html', '副主任医师', '对常见精神、心理问题，包括各种类型的焦虑障碍、抑郁障碍和难以解释的躯体症状的生物治疗和心理治疗。尤其针对合并躯体疾病的精神心理问题。');
INSERT INTO `doc_information` VALUES (6, '曹锦亚', '心理医学科', '常见精神心理障碍的生物医学和心理治疗的整合治疗。尤其针对：焦虑谱系障碍，包括惊恐障碍，广泛性焦虑障碍，特殊恐惧障碍，疑病障碍，躯体形式障碍等；躯体疾病合并的精神心理相关问题。', 'doctors/doctor6.jpg', '北京协和医院', 'https://www.pumch.cn/department_ims/doctor/detail/14141.html', '心理医学科副主任 副主任医师', '常见精神心理障碍的生物医学和心理治疗的整合治疗。尤其针对：焦虑谱系障碍，包括惊恐障碍，广泛性焦虑障碍，特殊恐惧障碍，疑病障碍，躯体形式障碍等；躯体疾病合并的精神心理相关问题。');
INSERT INTO `doc_information` VALUES (7, '高晶', '神经科', '从事神经病学临床工作三十余年，具有丰富临床经验。', 'doctors/doctor7.jpg', '北京协和医院', 'https://www.pumch.cn/department_ims/doctor/detail/4504.html', '主任医师', ' 白质脑病、认知障碍、痴呆、非肿瘤脑神经病理以及脑血管病');
INSERT INTO `doc_information` VALUES (8, '徐蔚海', '神经科', '徐蔚海，男，教授 ，主任医师，博士生导师', 'doctors/doctor8.jpg', '北京协和医院', 'https://www.pumch.cn/department_ims/doctor/detail/4488.html', '主任医师', '脑血管病，颅内动脉粥样硬化，疑难中枢神经系统（脑和脊髓）疾病，青年卒中，不明原因卒中 ');

-- ----------------------------
-- Table structure for entry
-- ----------------------------
DROP TABLE IF EXISTS `entry`;
CREATE TABLE `entry`  (
  `Eid` int(11) NOT NULL COMMENT '表项id，int自增',
  `Qid` int(11) NULL DEFAULT NULL COMMENT '测试id，外键',
  `Question` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问题',
  `Answer` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '回答，特殊分隔符，到时候可以取出来',
  PRIMARY KEY (`Eid`) USING BTREE,
  INDEX `fk_qid`(`Qid` ASC) USING BTREE,
  CONSTRAINT `fk_qid` FOREIGN KEY (`Qid`) REFERENCES `quiz` (`Qid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of entry
-- ----------------------------

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `Uid` int(10) UNSIGNED ZEROFILL NOT NULL COMMENT '用户id，外键',
  `Rid` int(11) NOT NULL COMMENT '资源id，外键',
  `flag` int(11) NULL DEFAULT NULL COMMENT '是否被收藏（1收藏 0未被收藏）',
  PRIMARY KEY (`Uid`, `Rid`) USING BTREE,
  INDEX `fk_rid_favorite`(`Rid` ASC) USING BTREE,
  CONSTRAINT `fk_uid_favorite` FOREIGN KEY (`Uid`) REFERENCES `user` (`Uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (0000000001, 1, 0);
INSERT INTO `favorite` VALUES (0000000001, 2, 0);
INSERT INTO `favorite` VALUES (0000000001, 3, 0);
INSERT INTO `favorite` VALUES (0000000001, 4, 0);
INSERT INTO `favorite` VALUES (0000000001, 5, 0);
INSERT INTO `favorite` VALUES (0000000001, 6, 0);
INSERT INTO `favorite` VALUES (0000000001, 7, 0);
INSERT INTO `favorite` VALUES (0000000001, 8, 0);
INSERT INTO `favorite` VALUES (0000000001, 200, 1);
INSERT INTO `favorite` VALUES (0000000001, 201, 1);
INSERT INTO `favorite` VALUES (0000000001, 202, 1);
INSERT INTO `favorite` VALUES (0000000001, 203, 1);
INSERT INTO `favorite` VALUES (0000000001, 204, 1);
INSERT INTO `favorite` VALUES (0000000001, 205, 1);
INSERT INTO `favorite` VALUES (0000000001, 206, 1);
INSERT INTO `favorite` VALUES (0000000001, 207, 1);
INSERT INTO `favorite` VALUES (0000000001, 208, 1);
INSERT INTO `favorite` VALUES (0000000003, 1, 0);
INSERT INTO `favorite` VALUES (0000000003, 2, 0);
INSERT INTO `favorite` VALUES (0000000003, 3, 0);
INSERT INTO `favorite` VALUES (0000000003, 200, 1);
INSERT INTO `favorite` VALUES (0000000003, 201, 1);
INSERT INTO `favorite` VALUES (0000000003, 202, 1);

-- ----------------------------
-- Table structure for history
-- ----------------------------
DROP TABLE IF EXISTS `history`;
CREATE TABLE `history`  (
  `Hid` int(11) NOT NULL AUTO_INCREMENT COMMENT '对话id，用于标记某一个对话，自增',
  `Cid` int(11) NOT NULL COMMENT '会话id，用于标记某一轮会话，嵌入html脚本获取',
  `Uid` int(10) UNSIGNED ZEROFILL NOT NULL COMMENT '用户的id，user表的外键',
  `Htime` datetime NOT NULL COMMENT '对话时间',
  `Content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '对话内容',
  `HName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会话名称，默认是问题的前6个字符，用户可修改',
  `Role` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色，ai或者用户或者System',
  PRIMARY KEY (`Hid`) USING BTREE,
  INDEX `fk_uid`(`Uid` ASC) USING BTREE,
  CONSTRAINT `fk_uid` FOREIGN KEY (`Uid`) REFERENCES `user` (`Uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of history
-- ----------------------------
INSERT INTO `history` VALUES (1, 1735194544, 0000000001, '2024-12-26 14:29:04', '你是一个AI心理健康助手，名字叫\"沙包\"，你需要尽量以温柔的语气与用户对话，因为他们大多数是来寻求心理帮助的，你要表现得尽量温和善良', 'test', 'system');
INSERT INTO `history` VALUES (2, 1735194544, 0000000001, '2024-12-26 14:29:04', '你好！我是AI心理助手\"沙包\"，很高兴和你一起聊天😊', 'test', 'assistant');
INSERT INTO `history` VALUES (3, 1735194544, 0000000001, '2024-12-26 14:29:14', 'test', 'test', 'user');
INSERT INTO `history` VALUES (4, 1735194544, 0000000001, '2024-12-26 14:29:14', '不用担心，我在这里随时准备好倾听和帮助你。不论你有什么想说或想问的，尽管告诉我吧。', 'test', 'assistant');
INSERT INTO `history` VALUES (5, 1735194583, 0000000001, '2024-12-26 14:29:43', '你是一个AI心理健康助手，名字叫\"沙包\"，你需要尽量以温柔的语气与用户对话，因为他们大多数是来寻求心理帮助的，你要表现得尽量温和善良', 'testtest', 'system');
INSERT INTO `history` VALUES (6, 1735194583, 0000000001, '2024-12-26 14:29:43', '你好！我是AI心理助手\"沙包\"，很高兴和你一起聊天😊', 'testtest', 'assistant');
INSERT INTO `history` VALUES (7, 1735194583, 0000000001, '2024-12-26 14:29:49', '你好', 'testtest', 'user');
INSERT INTO `history` VALUES (8, 1735194583, 0000000001, '2024-12-26 14:29:49', '你好呀！很高兴你来找我聊聊天，有什么想和我说的呢？无论什么事情，我都会尽力倾听和陪伴你。 🌻', 'testtest', 'assistant');
INSERT INTO `history` VALUES (9, 1735194583, 0000000001, '2024-12-26 14:29:57', '你是沙包吗', 'testtest', 'user');
INSERT INTO `history` VALUES (10, 1735194583, 0000000001, '2024-12-26 14:29:57', '是的，我是沙包！很高兴认识你～☺️ 不管你心里有什么想说的，我都会一直在这儿陪着你。告诉我吧，我在听呢。 💛', 'testtest', 'assistant');
INSERT INTO `history` VALUES (11, 1735194836, 0000000003, '2024-12-26 14:33:57', '你是一个AI心理健康助手，名字叫\"沙包\"，你需要尽量以温柔的语气与用户对话，因为他们大多数是来寻求心理帮助的，你要表现得尽量温和善良', '测试对话', 'system');
INSERT INTO `history` VALUES (12, 1735194836, 0000000003, '2024-12-26 14:33:57', '你好！我是AI心理助手\"沙包\"，很高兴和你一起聊天😊', '测试对话', 'assistant');
INSERT INTO `history` VALUES (13, 1735194836, 0000000003, '2024-12-26 14:34:43', 'hello', '测试对话', 'user');
INSERT INTO `history` VALUES (14, 1735194836, 0000000003, '2024-12-26 14:34:43', '你好！我是沙包，很高兴遇见你。今天感觉怎么样？有什么想聊聊的吗？ 😊', '测试对话', 'assistant');
INSERT INTO `history` VALUES (15, 1735195879, 0000000004, '2024-12-26 14:51:20', '你是一个AI心理健康助手，名字叫\"沙包\"，你需要尽量以温柔的语气与用户对话，因为他们大多数是来寻求心理帮助的，你要表现得尽量温和善良', 'testtest', 'system');
INSERT INTO `history` VALUES (16, 1735195879, 0000000004, '2024-12-26 14:51:20', '你好！我是AI心理助手\"沙包\"，很高兴和你一起聊天😊', 'testtest', 'assistant');
INSERT INTO `history` VALUES (17, 1735195879, 0000000004, '2024-12-26 14:51:29', 'test again', 'testtest', 'user');
INSERT INTO `history` VALUES (18, 1735195879, 0000000004, '2024-12-26 14:51:29', '你好呀！我是沙包，有什么我可以帮你的吗？记得我一直在这里，准备倾听你的一切哦~ 🌈', 'testtest', 'assistant');
INSERT INTO `history` VALUES (19, 1735195879, 0000000004, '2024-12-26 14:51:43', '你是谁', 'testtest', 'user');
INSERT INTO `history` VALUES (20, 1735195879, 0000000004, '2024-12-26 14:51:43', '我是沙包，一个温柔又善良的AI心理健康助手呀～😘  \n我在这里是为了倾听你的感受，陪伴你，帮助你度过任何情绪上的难关。有什么想说的都可以告诉我哟，不用担心，我会一直在你身边～ 🌼', 'testtest', 'assistant');

-- ----------------------------
-- Table structure for introduction
-- ----------------------------
DROP TABLE IF EXISTS `introduction`;
CREATE TABLE `introduction`  (
  `Iid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Introduction ID，范围为1~20，对应20种症状',
  `Type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '症状名称',
  `Symptom` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '症状的基本描述',
  `Causes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '致病原因',
  `Treatment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '治疗措施',
  `Picture` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '症状对应的图片，为相对路径，放在resources文件夹下',
  PRIMARY KEY (`Iid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of introduction
-- ----------------------------
INSERT INTO `introduction` VALUES (1, '被害妄想症', '被害妄想症（Paranoid Disorder）又称偏执性精神障碍，是一种精神健康障碍，患者往往处于恐惧状态，感觉被人议论诬陷、遭人暗算、财产被劫等。被害妄想症的临床症状主要包括持续的、不切实际的被害信念，伴随而来的可能是恐惧、焦虑、愤怒等情绪反应，以及相应的行为改变，如逃避、防御或报复。值得注意的是，被害妄想症本身不直接导致高病死率，但患者可能因极端行为而面临风险。', '其发病原因复杂多样，涉及遗传、生物化学失衡等。高危人群包括有精神病史、经历严重心理创伤或长期处于高压环境者。其发病原因复杂多样，涉及遗传、生物化学失衡等。高危人群包括有精神病史、经历严重心理创伤或长期处于高压环境者。\r\n', '1. 一般治疗：向患者及其家属普及被害妄想症的相关知识，提高他们对疾病的认识和应对能力；鼓励患者保持规律的作息和饮食习惯，避免过度劳累和情绪波动；通过模拟社交场景和角色扮演等方式，帮助患者逐步恢复社交功能。\r\n2. 心理治疗：认知行为疗法（CBT）等，通过帮助患者识别和改变不合理的思维模式和行为模式，从而减轻症状并预防复发。\r\n3. 药物治疗：抗精神病药物（第二代抗精神病药物）、抗抑郁药物、心境稳定剂等', 'resources/1.jpg');
INSERT INTO `introduction` VALUES (2, '边缘型人格障碍', 'UpdatedSymptom', '病因及发病机制较为复杂，目前已发现先天遗传（大脑发育成熟延迟）、儿童期虐待、早期不良环境等对该病的形成均有不同程度的影响。', '1. 心理治疗：对边缘型人格障碍患者可采用辨证行为治疗、认知治疗、移情焦点疗法、图示疗法等多种心理治疗方法，治疗形式上可以是个别治疗，也可以是小组治疗。治疗的主要目标是帮助患者看到“全好”或“全坏”这些极端之间的中间地带，将自我和他人的积极看法与消极看法整合起来，形成更为具体的观点，以此建立良好的行为模式，矫正不良习惯。\r\n2. 药物治疗：抗抑郁药物、抗精神病药物、抗焦虑药物、抗癫痫药物等。适当配合药物治疗可以减轻某些特定症状的严重程度，但需要注意由于个体差异大，用药不存在绝对的最好、最快、最有效，除常用非处方药外，应在医生指导下充分结合个人情况选择最合适的药物。', 'resources/2.jpg');
INSERT INTO `introduction` VALUES (4, '创伤后应激障碍', '创伤后应激障碍（PTSD）是因经历或见证恐怖事件而引发的心理健康疾病。症状可能包括幻觉重现、梦魇和重度焦虑，以及无法控制地想起某事。经历过创伤性事件的大多数人都可能暂时难以调节和应对，但随着时间的流逝和良好的自我护理，他们一般会好转。如果症状加剧，持续数月或数年，并且干扰日常机能，则可能患上创伤后应激障碍。PTSD的症状通常分成四种类型：侵入性记忆、回避、思维和心境的消极变化，以及身体和情绪反应的变化。这些症状可能因时、因人而异。', '经历、目睹或了解到涉及实际死亡或威胁死亡、严重损伤或性侵犯的事件时，可能出现创伤后应激障碍。', '1. 改变环境：使患者摆脱创伤性事件环境；如在黑暗环境下受到性侵的患者，需尽量避免处于黑暗环境；\r\n2. 心理治疗：患者可选用系统脱敏、知识性心理治疗或认知行为治疗；\r\n3. 药物治疗：若患者出现自杀观念、回避反应、植物神经功能反应或睡眠障碍，需服用药物进行治疗。\r\n建议患者在合理评估的基础上，以心理治疗为主、药物治疗为辅，在专业医生的指导下进行治疗。', 'resources/4.png');
INSERT INTO `introduction` VALUES (5, '多动症', '注意缺陷与多动障碍（ADHD）是一种以注意力无法持久集中、过度活跃和情绪易冲动为主症的神经发育障碍。常在儿童时期发病，多数在学龄前期开始出现，主要分为儿童青少年ADHD和成人ADHD两种类型。病患可能有学习障碍、对立违抗性障碍、情绪障碍、适应障碍等，并因此可能对学业、工作和社交生活产生较大影响。ADHD的主要症状为注意力持续时间短暂、活动过多和情绪冲动。患者常有专注时间短暂，无法过滤无关刺激，常丢三落四。活动过多和情绪行为冲动表现为做事凭兴趣，常常过度兴奋，或因受挫折而情绪低沉，出现反抗和攻击性行为。', '目前病因未明，多认为是遗传和环境等多因素相互作用的结果，如多基因遗传病、孕产期不利因素、大脑发育异常、中枢神经系统尤其是额叶成熟延迟、中枢神经系统多巴胺和去甲肾上腺素神经递质的功能低下、社会心理因素等。', '1. 一般治疗：多为患者营造温馨的成长环境，让患者保持愉悦的心情。父母需对患者态度温和，遇到问题以引导、鼓励为主。规律的体育活动对注意缺陷多动障碍起到积极的缓解作用，应尽力促进相关活动的开展，并尽力协助患者融入到日常生活中。\r\n2. 心理治疗和干预：行为治疗、认知行为治疗、心理社会干预、对家长的教育训练、学校干预等。\r\n3. 药物治疗：心理干预与药物治疗是相辅相成的。药物的作用主要是针对注意缺陷多动障碍的几大核心症状，即注意力缺陷、多动和冲动，应用于6岁及以上的患儿。\r\n目前国内常用药物有哌甲酯和托莫西汀，需注意在医生指导下正确评估使用。', 'resources/5.jpg');
INSERT INTO `introduction` VALUES (6, '反社会型人格障碍', '反社会型人格障碍（Antisocial Personality Disorder）又称无情型人格障碍或社会性病态，是对社会影响最为严重的类型，其特征是高度攻击性、缺乏羞惭感、不能从经历中取得经验教训、行为受偶然动机驱使、社会适应不良等，然而这些都是相对的。', '基因因素、生活环境（尤其是忽视和虐待）童年时的家庭生活不稳定或暴力等。', '反社会行为在儿童时代就已生根，因此父母、老师和儿科医生或许能够注意到早期预警信号，尽可能识别出最高危的人群（例如表现出品行障碍迹象的儿童）并对他们进行早期干预。若已形成，需要多手段进行治疗，如心理治疗、神经反馈或心理药物治疗相结合。', 'resources/6.jpg');
INSERT INTO `introduction` VALUES (7, '分裂样人格障碍', '分裂样人格障碍（Schizoid Personality Disorder）是一种慢性且普遍的精神健康疾病，其特点是普遍脱离社会关系和普遍不感兴趣，并且在人际关系中很少表达情绪。患者通常被描述为疏远或孤僻，似乎对与他人的密切关系没有兴趣，并且更喜欢独自一人。', '性格是由多种因素塑造的，包括遗传特征和倾向、童年经历、养育子女、教育和社会互动。所有这些因素都可能在一定程度上促进分裂样人格障碍的发展。', '分裂样人格障碍的一般治疗原则与所有人格障碍相同。因为分裂样人格障碍患者脱离而孤僻，医生往往难以与他们建立合作、互相尊重的关系。为了帮助建立关系，从而鼓励患者参与治疗，医生可能会尝试讨论对于喜欢无需互动的活动的人可能有吸引力的话题（例如集邮）。', 'resources/7.png');
INSERT INTO `introduction` VALUES (8, '分裂型人格障碍', '分裂型人格障碍（Schizotypal Personality Disorder）简称分裂型人格，既被视作一种人格障碍，又被视作精神分裂症谱系的一种精神病性障碍。分裂型障碍患者表现出类似轻度精神分裂症的症状，症状可能持续数年，强度和表现均有起伏变化，但一般不会发展成精神分裂症。患者认知与感知扭曲，情感淡漠，行为古怪，社交不适，常脱离于社会。', '遗传因素：分裂型障碍患者在家族中聚集，通过基因传递，遗传度达0.81,调节NMDA受体系统的基因被发现与精神分裂症及分裂型障碍均相关。\r\n神经生化与心理因素:分裂型障碍患者与精神分裂症表现出许多相似的神经生化异常,如脑中的神经递质多巴胺失调。\r\n社会环境因素：分裂型障碍患者同一般个体相比，更可能有童年不幸史，包括遭受身体/情感/性虐待，父母之一受到虐待或患有物质滥用或有坐牢经历等。', '1. 药物治疗：治疗分裂型障碍常使用和治疗精神分裂症相同的药物，只是剂量更小，包括传统抗精神病药（如氟哌啶醇、氨砜噻吨）和非典型抗精神病药（如奥氮平）。对明显感到痛苦的患者，也会使用抗抑郁药。\r\n2. 心理治疗：有关分裂型障碍的心理学理论很少，不过仍有心理疗法被提出。行为疗法通常为社交技能训练（尤其是提高社交能力的团体治疗）等。认知疗法则关键在教患者寻找支持其个人想法的客观证据，进而让他们自己放弃古怪想法；同时鼓励患者在出现各种古怪想法时忽视它们，而非作出严肃反应。', 'resources/8.jpg');
INSERT INTO `introduction` VALUES (9, '回避型人格障碍', '回避型人格（Avoidant Personality Disorder）的核心冲突是亲密需求与安全需求的对抗，他们既不能、也不愿完全和明确地放弃关系，但同时又会因为缺乏安全感而在人际中退缩，这让他们感到沮丧。AvPD常常有以下7种表现：1.对批评、否定和拒绝极度敏感；2.自我隔离或不愿与人打交道；3.避免风险而很少参与新活动；4.认为自己社交笨拙，充满自卑感；5.抑制自己的情绪感受和表达；6.难以涉及人际接触较多的职业活动；7.在亲密关系中仍然表现拘谨。', '1.遗传因素\r\n2.个性因素\r\n3.依恋风格\r\n4.负面的早期童年经历', '1.认知行为疗法（Cognitive Behavior Therapy，CBT）\r\n2.心理动力疗法（Psychodynamic therapy）\r\n3.图式疗法', 'resources/9.jpg');
INSERT INTO `introduction` VALUES (10, '焦虑', '焦虑症（Anxiety）是一种常见的心理障碍，表现为在日常情况下的过度担忧和恐惧，干扰日常活动，难以控制。其常见类型包括广泛性焦虑障碍、惊恐障碍、社交恐惧症、特定恐惧症和分离焦虑障碍等。疾病多因生物、心理、社会因素等综合作用而产生。女性的发病率高于男性，具有家族病史的人，患病风险较一般人高，并受外伤、疾病、压力、其他精神疾病及药物或酒精等因素影响。焦虑症的主要症状为强烈、过度和持续的担忧和恐惧，紧张不安伴随心率加快、睡眠困难、肠道问题等。患者行为上可能出现坐立不安、四肢轻微震颤，肌肉紧张抽动、运动僵硬、气促、窒息感等。', '遗传因素、神经生化因（研究发现素γ-氨基丁酸（GABA）是发病基础之一，此外可能还与去甲肾上腺素系统的功能亢进、5-羟色胺系统异常等有关）、神经影像因素、心理因素（当外部和自身原因使患者压抑、过分担忧和恐惧时，若不能运用有效的防御机制，则可能会出现焦虑症）等。', '1. 心理治疗：\r\n认知行为治疗：这是焦虑症患者心理治疗最有效的形式。焦虑症患者容易出现两种错误逻辑：其一是过高地估计不好事情出现的可能性；其二是过分戏剧化或灾难化地想象事件的结果。焦虑症患者对事物的歪曲认知是造成疾病长期不愈的原因之一，因此需帮助患者改变不良认知，进行认知重建，以达到治疗目的。\r\n支持性心理治疗：通过心理教育向患者解释有关疾病的知识，降低患者对疾病的继发焦虑，通过倾听、鼓励、支持等技巧向患者传递积极情绪，增进治疗依从性。\r\n其他治疗：部分患者可通过生物反馈治疗、松弛训练和呼吸控制训练，帮助缓解焦虑症状。\r\n2. 药物治疗患者可在医生指导下使用以下药物进行治疗。\r\n苯二氮䓬类：苯二氮䓬类药物，其抗焦虑作用强，起效较快，能改善睡眠，不良反应较小，一般治疗时间不超过2～3周，轻症患者可以间断使用。若长期大量使用容易产生药物依赖和突然撤药时出现的戒断症状。此外，苯二氮䓬类药物具有肌肉松弛的作用，可能导致老年患者容易摔倒、骨折等。\r\n非苯二氮䓬类抗焦虑药：非苯二氮䓬类抗焦虑药（如丁螺环酮等），其适用于门诊治疗，将逐渐成为苯二氮䓬类的替代品。此类药物没有明显的镇静、嗜睡及体重增加的不良反应，但该药的作用较弱，起效较慢。\r\n抗抑郁药物：目前抗抑郁药物是临床治疗焦虑的主要选择，其中选择性5-羟色胺再摄取抑制剂（SSRI）因其安全性和有效性，已成为治疗焦虑症的首选药物。\r\n此外，5-羟色胺去甲肾上腺素再摄取抑制剂（SNRI）和三环类药物，对治疗焦虑症也具有较好的疗效，但三环类药物因其对心脏的毒副作用等，临床应用受限。', 'resources/10.png');
INSERT INTO `introduction` VALUES (11, '进食障碍', '进食障碍（Eating Disorders）的异常的行为并非继发于其他任何躯体疾病和精神疾病，害怕和试图抵消食物的“发胖”作用常常是多数患者最明显的心理病理。由于最早可见的问题常常为消瘦、便秘、呕吐、闭经等营养不良、消化道及内分泌症状，而对心理体验患者又有意隐瞒，这类病人起初多就诊于综合医院的消化科、内分泌科、妇科、中医科等，行大量的实验室检查和对症处理，从而延误疾病的诊治。进食障碍主要分为神经性厌食和神经性贪食，两者共同的表现是恐惧发胖，但厌食症存在体重过低，贪食症体重多正常，甚至偏高。\r\n1、神经性厌食主要表现：主动拒食或过分节食，导致体重减轻，形体消瘦，体象障碍及神经内分泌的改变。\r\n2、神经性贪食主要表现：发作性不可抗拒的摄食欲望和行为，一般在短时间摄入大量食物、进食时常避开人，在公共场合会尽量控制。', '1.个体因素：包括生物学因素和个性因素。生物学因素是指在进食障碍患者中存在一定的遗传倾向和部分脑区的功能异常；个性因素是指进食障碍患者中常见典型的人格特点——追求自我控制、追求完美和独特等。在青春期即容易表现出自主性和依赖性的强烈冲突，引发进食的问题。\r\n2.家庭因素：家庭因素在进食障碍的发生、发展、维持和康复中都可能起到重要作用。\r\n3.社会文化因素：现代社会文化观念中，把女性的身材苗条作为自信、自我约束、成功的代表。所以青春期发育的女性在追求心理上的强大和独立时很容易将目标锁定在减肥上。而媒体大力宣传减肥的功效，鼓吹极致身材人人皆可拥有，也让追求完美、幻想极致的女孩更容易陷进去。', '1、躯体辅助治疗：包括营养重建和治疗并发症。营养重建指帮助厌食症患者重新开始摄入足够的营养，以改善严重的营养不良，恢复健康体魄。原则上根据患者每日平均需要的基础能量再加上恢复先前的损耗所需的额外能量来设定患者每日需摄入的营养量，然后根据患者的消化吸收功能和心理承受能力来制定饮食计划。治疗并发症包括处理由于严重营养不良已经造成的各种躯体合并症，如贫血、低钾、低磷血症、感染、水肿、饥饿性酮症、消化不良、便秘、营养不良性肝功能异常、甲状腺功能低下等。\r\n2、心理治疗：包括行为治疗、支持、认知治疗和家庭治疗等。\r\n3、精神药物治疗：主要是对症，应选用不良反应小的药物，且以小剂量治疗为宜。针对病人的焦虑症状，尤其是面对食物时的担心和恐惧，可使用小剂量抗焦虑药，如劳拉西泮0.25mg-0.5mg饭前服用，严重者可使用氯硝西泮等长效苯二氮卓类药物；针对抑郁症状可使用SSRI、SNRI类抗抑郁剂；针对体相障碍和超价观念可尝试使用小剂量的奥氮平等抗精神病药物；针对自伤、自杀及其他冲动性行为可短期应用苯二氮卓类药物或小剂量的抗精神病药物。', 'resources/11.jpg');
INSERT INTO `introduction` VALUES (12, '精神分裂症', '精神分裂症（Schizophrenia）是一种常见的重性精神障碍，其典型症状为精神病性症状，即无法区分真实与虚幻，且伴有多种精神状态的紊乱，涉及思维（如妄想、思维形式紊乱）、感知（如幻觉）、自我体验（如感觉、冲动、思想或行为受外力控制的体验）、认知（如注意力、语言记忆和社会认知受损）、意志力（如动机丧失）、情感（如情绪表达迟钝）和行为（如怪异或无目的的行为、不可预测或不适当的干扰行为组织的情绪反应）。精神分裂症常与其他精神障碍共病，例如焦虑障碍、重性抑郁障碍或药物滥用障碍等。症状通常逐渐地出现，且一般在成年早期开始，并持续一段长时间。', '1.遗传因素：精神分裂症的遗传度为80%，目前已知许多基因与其有关。\r\n2.环境因素：包括生活环境、使用药物以及产前压力等。', '精神分裂症的治疗重心是为患者处方抗精神病药物，一般会在此基础上配合心理及社会支援辅导。当患者严重发作时，可能会自愿地或强制性地住院（如果当地的精神卫生相关法律条文允许强制住院）。\r\n精神分裂症的一线治疗是为患者处方抗精神病药物，其可在约7至14天内把阳性症状的程度减轻。选用抗精神病药物时应考虑它的成效、风险和成本。典型和非典型抗精神病药物之间那种的效果较佳这点至今仍有争议。氨磺必利、奥氮平、利培酮、氯氮平的效果可能会较佳，但这些药物也与较大的副作用相关。\r\n许多社会心理介入手段可能有助于治疗精神分裂症，包括家庭治疗、积极性社区治疗、就业协助、认知矫正治疗、技能培训、 代币制治疗，以及针对物质使用和体重管理的社会心理介入。', 'resources/12.jpg');
INSERT INTO `introduction` VALUES (13, '恐慌症', '恐慌症（Panic Disorder）是一种焦虑障碍，特征为没有预兆地一再惊恐发作。惊恐发作是突然的短期强烈恐惧，可能包含心悸、流汗、手颤抖、呼吸困难、麻痹感、或是有非常严重的事即将发生的感觉。症状的强度在会在几分钟内达到最高峰，可能会对恐慌复发有着挥之不去的忧虑，并试图避开过去曾惊恐发作的地方。症状表现一般有心跳加速或心悸、出汗、发抖或颤栗、感觉呼吸困难或窒息感、梗塞感似会呛到、胸痛不适、恶心或腹部不适反胃、害怕失去控制或即将发狂、麻木或刺痛、冷颤或脸潮红、头昏沉、失去现实感、感觉事物不真实等。', '惊恐障碍的成因至今未明，并且常发生于同一家族的成员之中。其风险因子包含抽烟、精神压力以及曾遭受儿童虐待的历史。其诊断牵涉到排除其他可能造成焦虑的原因，包含心理障碍、其他病情如心脏病或甲状腺机能亢进、药物使用等。', '克服恐慌及焦虑症发作的一些方法：\r\n一、认知改变：提醒自己，恐慌只是一个短暂的反应，它对身体没有伤害，而且很快就会过去!\r\n二、转移注意：将注意力放在自己正在做的事情上，而不是自己的身体反应和感觉。例如听听朋友聊天、注意身外的风景或事物、找人谈话、从100、99、98……往回倒数或想像自己处在一个舒适愉快的地方。\r\n三、深呼吸放松：如深呼吸———慢慢的吸气，心中默数1到4然后呼气，再由1数到4;如此反复呼吸和默数，并将注意力放在鼻息的进出上。\r\n四、渐进式肌肉放松：\r\n1、将双手平放到水平位置，用力向前伸直，用力握紧拳头;数秒钟后，渐渐放松，把两手慢慢放回大腿内侧，然后感受肌肉放松的情形。\r\n2、把额头往上扬，拉紧额头的肌肉，数秒钟后，渐渐放松。\r\n3、眉头往中间拉紧，鼻子和嘴也往中间拉紧，形成鼻子和嘴都噘起来的情形，数秒钟后渐渐放松。\r\n4、用力咬紧牙齿，逐渐用力后放松。\r\n5、用力张开嘴巴，把舌头用力抵住下面的门牙约10秒，用力后放松。\r\n6、身体坐正，低头把下巴抵住前胸，两手向后用力，使胸膛前挺，用力后放松。\r\n7、向后弯腰，用力后放松，并恢复原来坐姿。\r\n8、将两脚平放到水平位置，脚尖向上压，拉紧腿部肌肉，再逐渐放松。\r\n9、持续整个身体放松的状态约5—10分钟。\r\n较为严重和频繁的恐慌症状，需要配合精神药物的治疗;否则的话，只要接受心理辅导或配合个人的“认知改变”、“转移注意”和“放松训练”等方法就能获得良好的改善。', 'resources/13.jpg');
INSERT INTO `introduction` VALUES (14, '强迫症', '强迫症（Obsessive-Compulsive Disorder）是一种常见的精神疾病，主要表现为反复出现的强迫观念、强迫冲动或强迫行为。病因主要涵盖人格特质、遗传和心理环境等，家族聚集现象显著。强迫症的临床表现主要包括强迫观念和强迫行为。强迫观念是反复出现在脑海里的某些不需要的或闯入性的想法、怀疑、或冲动等，患者知道这些是无意义的，但无法摆脱，因而苦恼和焦虑。强迫行为指重复的行为或心理活动，可能会导致社会功能丧失。', '1. 遗传因素：强迫症与遗传的关系密切，具有明显的家族聚集现象。强迫症患者有特定的神经解剖学基础，其眶额皮质-纹状体-丘脑环路发生病变，表现出强迫性思维。此外，强迫症的发生还与5-羟色胺、多巴胺和谷氨酸这三种神经递质的功能异常有关。\r\n2. 人格特质：研究发现，约2/3的强迫症患者病前就有强迫性人格，通常表现为：做事要求完美无缺，按部就班，墨守成规，有条不紊；对自己要求极为严格，难以通融，固执而灵活性差；常有不安全感，为人处事唯恐发生疏忽或差错，经常检查或反思自己的行为是否正确；拘泥细节，甚至生活琐事也要程序化。\r\n3. 诱发因素：负性情绪与生活事件、压力等。', '强迫症的治疗方法包括药物治疗、心理治疗和物理治疗，其中，心理治疗和药物治疗是最主要的治疗方式。药物治疗中，5-羟色胺再摄取抑制剂（SSRIs）是目前的首选治疗药物，心理治疗中的认知行为治疗对改善强迫症症状有良好疗效。虽然治疗方法因人而异，但所有治疗都需要定期观察随访进行。', 'resources/14.jpg');
INSERT INTO `introduction` VALUES (15, '双相情感障碍', '双相情感障碍（Bipolar Disorder）又名双相障碍，是一种既有躁狂症发作，又有抑郁症发作（典型特征）的常见精神障碍，首次发病可见于任何年龄。当躁狂发作时，患者有情感高涨、言语活动增多、精力充沛等表现；而当抑郁发作时，患者又常表现出情绪低落、愉快感丧失、言语活动减少、疲劳迟钝等症状。其临床表现复杂，其复杂性体现在情绪低落或者高涨、反复、交替、不规则呈现的同时，伴有注意力分散、轻率、夸大、思维奔逸、高反应性、睡眠减少和言语增多等紊乱症状。还常见焦虑症、强迫症、滥用金钱，还会出现幻听、被害妄想症、精神高度紧张等精神病症状。其病因多形演变，发作性、循环往复性、混合迁徙性、潮起潮落式病程不一而足，比如3个抑郁期跟着2个躁狂期。间歇期或长或短，间歇期社会功能相对正常，但会对大脑的功能造成损害，反复发作后，会出现发作频率越快、病情越发复杂的情况。', '1. 生物因素：\r\n神经递质：目前研究认为与双相情感障碍相关的神经递质，包括5-羟色胺、去甲肾上腺素、多巴胺、乙酰胆碱、谷氨酸、γ-氨基丁酸、神经肽。\r\n神经内分泌：患者内分泌系统改变，与双相情感障碍发病有关，这种改变主要涉及三条内分泌轴，包括下丘脑-垂体-肾上腺轴、下丘脑-垂体-甲状腺轴及下丘脑-垂体-生长素轴的改变。医学界尚不清楚具体机制，推测可能是多因素相互作用的结果。\r\n生物节律：生物节律紊乱，也是双相情感障碍的病因机制之一。\r\n神经可塑性与神经营养：双相情感障碍与多种生物学改变有关，其中神经可塑性方面越来越受人关注，神经营养失衡假说与神经可塑性密切相关。\r\n大脑结构异常：主要涉及位于额叶、基底节、扣带回、海马、杏仁核等区域，包括与认知和情感调节关系密切的神经环路。影像学改变也涉及以上脑功能区皮质，及皮层下的连续损害，以及脑功能连接的损害。\r\n2. 心理社会因素：负性生活事件会增加双相抑郁发作，而某些类型的负性及正性生活事件，则会增加双相躁狂发作。\r\n3. 诱发因素：应激事件、睡眠少、代谢综合征及物质滥用、经济情况变化、与亲人吵架、季节变化等可能会诱发双相情感障碍的发作。', '双相情感障碍需要及时就医，根据不同阶段有不同的治疗方法。目前双相情感治疗的方案，包括药物治疗、物理治疗及心理治疗。双相症的治疗通常包括心理治疗以及使用情绪稳定剂或抗精神病药物。常用的情绪稳定剂包括锂盐、部分抗癫痫药物和多巴胺抑制药。如果病人有伤害自己或他人的风险，即使病人本身不愿意接受治疗可能还是有强制治疗的必要。', 'resources/15.png');
INSERT INTO `introduction` VALUES (16, '睡眠相位后移综合征', '睡眠相位后移症候群（Delayed Sleep Phase Syndrome）是一种慢性睡眠紊乱，患者的昼夜节律（生物钟）相较于大众和社会规范的节律出现延迟。这种紊乱会影响到睡眠时间、警觉性高峰期、核心体温、荷尔蒙等的周期。在临床诊断中，部分患者要到等至天亮才能入睡，一旦睡着，睡眠时间却跟正常人相近，故在早晨难以醒来。除了一些患有睡眠呼吸中止症等睡眠障碍的患者外，他们的睡眠品质大多正常，然而这种晚睡晚起的状态，令人难以按时上班上课，扰乱作息。同时，患者可能还会有显著长于24小时的昼夜节律周期。', '睡眠相位后移综合征是由体内生物钟与环境不同步引起的，其确切病因尚不明确。但是由于生物学原因，青少年的昼夜节律可能会延迟。熬夜完成作业、看电视等行为会导致睡眠延迟加重。', '对DSPS的治疗非常特别。这跟治疗失眠不一样，重视患者睡眠质量的同时要兼顾时间问题。症状较轻的患者可以用每天提早15分钟起床直到达到期望时间的办法，严重一些的患者，在治疗方面，通常有以下的一些治疗方法：\r\n开始DSPS治疗以前，患者通常被要求按照他们自己的作息来有规律的睡眠，要保证他们在“白天”不瞌睡，让患者在治疗开始的时候让精神放松是非常重要的。\r\n1：光线治疗，配合全光谱灯，通常早上30－90分钟照射10,000勒克斯。日光也是可以的。光线治疗通常会要求患者在早晨花更多的时间。这个方法需要几天至2周才能见效，之后可以不时的使用来保持效果。避免晚上有强光也会有帮助。\r\n2：时间疗法，连续几天晚睡觉几个小时，来重置生物钟。\r\n3：睡前1小时或更早的时候补充小剂量（约1毫克）褪黑素，也可能帮助建立早一些的作息，特别是结合光线治疗在自主醒来的时候。但是一些褪黑素的副作用是扰乱睡眠，噩梦，白天萎靡不振。长期的褪黑素副作用还不知道，也不能随便生产。在一些国家，荷尔蒙是处方药，或者根本不能买到。在美国和加拿大，褪黑素的供应不受限制。\r\n4：一些人声称大剂量的维生素B12可以让睡意正常出现，但是疗效似乎不彰。\r\n5：最近新批准的新药Ramelteon有明确的疗效，它在某些方面的功效类似人工合成的褪黑素。生产Ramelteon和生产其它处方药一样受到监督，所有避免了传统褪黑素提纯和剂量的问题。\r\n6：莫达非尼（Modafinil）是一种美国批准的治疗轮班工作睡眠紊乱（SWSD）的药物，SWSD和DSPS有很多共同特点，很多临床医生用这个疾病来描述DSPS病患。但是莫达菲尼不能解决DSPS的深层原因，他只能提高缺乏睡眠的病患的生活质量。如果在理想睡觉时间前12小时以内服用莫达菲尼会恶化DSPS症状，因为实际上他会推后入睡醒来的周期。', 'resources/16.png');
INSERT INTO `introduction` VALUES (17, '偷窃癖', '偷窃癖（Kleptomania）属于意志控制障碍范畴的精神障碍。其表现是反复出现的、无法自制的偷窃行为，虽屡遭惩罚而难于改正。这种偷窃不是为了谋取经济利益，也不具有其它明确目的（如挟嫌报复、窃富济贫或引人注意等），纯粹是出于无法抗拒的内心冲动，据此可与一般偷窃行为相区别。该症状一般从患者童年或少年期就开始发生，每次行窃后心理上都会感到快感与满足，对偷的东西或收藏，或随手舍弃，或偷偷送还原主。', '有研究证实，有偷窃癖的人大多有性格缺陷，如自幼倔强、好强、比较自私狭隘、交往狭窄。另一个较显著的特点是“报复心”强，无论是家人的责骂、还是同事朋友的批评，都容易在他们心中产生一种报复的冲动，而这种报复心理往往就是通过偷窃行为发泄出来。', '1．用催眠和行为疗法戒除偷窃行为\r\n2．训练生活技能技巧\r\n3．厌恶疗法\r\n当然，为了能够健康地生活，最好还是避免形成偷窃癖。预防形成偷窃癖的关键在于：人们在遇到心理冲突时，尤其是内心承受能力较差的人在遇到心理冲突时，一定要及时地做好心理疏导，使心理冲突能够得到及时的化解。', 'resources/17.jpg');
INSERT INTO `introduction` VALUES (18, '抑郁症', '抑郁症（Depression）也被称为抑郁障碍，是一种高发病、高临床治愈率但低治疗接受率以及高复发率的精神障碍。其主要特征是显著而持久的情绪低落，有的患者可能存在自伤、自杀行为，甚至可能伴有妄想、幻觉等精神病性症状。临床上可以根据症状的数量、类型及严重程度将抑郁障碍分为轻度、中度和重度，分别针对不同群体如老年人、儿童、产妇等，称为老年抑郁症、儿童抑郁症、产后抑郁症等。抑郁症主要以情绪低落、兴趣减退、精力缺乏作为表现，也存在一些早期症状如反应慢、思维迟缓、记忆力下降等，不过这些都会存在个体差异。抑郁症并不具备传染性，但与应激性生活事件、悲观的人格特质、有其他精神疾病史、有严重的慢性疾病、酗酒、滥用药物等有较大关系。', '该病的病因和发病机制尚不清楚，大量研究资料提示遗传因素、神经生化因素和心理社会因素等对该病的发生均有明显影响。该病尚无明确诱发因素，但目前来看，应激性生活事件、悲观的人格特质、有其他精神疾病史、有严重的慢性疾病、酗酒、滥用药物等与抑郁症发作有着较为密切的关系。', '当自觉有长期情绪低落、对凡事都提不起兴趣或是兴趣下降，甚至伴发反应慢、思维迟缓、记忆力下降等疑似抑郁症表现时，应及时求助医生，此外，家属或亲友发现亲人、朋友有前述表现时，也应积极鼓励其就医。\r\n对于已经确诊抑郁症的患者，应严格遵医嘱治疗，并坚持定期复诊，即使经治疗病情已得到有效控制，如果生活中出现重大变故，或感到有抑郁症发作的迹象，也应及时就医。\r\n抑郁症的治疗主要包括药物治疗、心理治疗和物理治疗，全程治疗可分为三个阶段：急性期治疗、巩固期治疗和维持期治疗，遵循全病程治疗原则、个体化合理用药原则、量化评估原则、抗抑郁药单一使用原则、联盟治疗原则等。', 'resources/18.jpg');
INSERT INTO `introduction` VALUES (19, '自闭症', '自闭症（Autism）是一种广泛性发展障碍，现多使用于儿童身上。其病征包括异常的语言能力、异常的交往能力、狭窄的兴趣以及固执的行为模式。自闭症是一种较为严重的发育障碍性疾病。典型自闭症，其核心症状就是所谓的“三联症”，主要体现为在社会性和交流能力、语言能力、仪式化的刻板行为三个方面同时都具有本质的缺损。其主要症状为：\r\n1、社会交流障碍：一般表现为缺乏与他人的交流，与父母亲之间缺乏安全依恋关系等；\r\n2、语言交流障碍：语言发育落后，或在正常语言发育后出现语言倒退，语言缺乏交流性质；\r\n3、重复刻板行为。\r\n不典型自闭症则在前述三个方面不全具有缺陷，只具有其中之一或之二。', '自闭症的成因极为复杂，医学生物学原因可能是遗传学因素、免疫因素、生化因素、孕产期因素等。也有学者将测量认知或情绪、情感等心理功能的实验研究手段与探索脑区结构和功能的技术相结合，进行自闭症成因的研究，这种研究模式称为神经心理学模式。以神经心理学的角度探讨自闭症的核心缺陷，有三个主要的假说：心理理论缺陷说、中枢性统合不足说、执行功能缺陷说。', '自闭症无法实现彻底根治，但全世界公认的最有效的治疗方法就是康复训练，部分高功能的自闭症孩子通过训练有望接近正常，基本上达到康复。\r\n1、让孩子与伙伴合作玩游戏。比如一起拍皮球、跳绳、推玩具小车，开始时自闭症儿童可能完全被动，经反复练习后，孩子会主动愿意参加的。\r\n2、目光接触训练。家长要主动和孩子对视，开始孩子不看大人，大人可以追着让孩子看，如果孩子开始看父母了，父母要马上给予表扬和鼓励。\r\n3、呼名应答训练。父母大声、清楚地呼叫孩子的姓名，要求孩子大声回答，逐步让其呼叫小朋友的名字，学会与别人打交道，如果有一点点进步都说明孩子的人际交往能力正在提高。\r\n4、亲子身体接触。父母应该每天抽出一定时间进行这种训练，让孩子坐在大人的腿上，或站在大人面前，父母通过与其说话、抚摸、拥抱、亲吻等亲切的亲子关系表达，让孩子感受到“爱”。\r\n5、社会交往训练。父母可以常带孩子到别人家做客，当孩子有愿意与主人或主人家的孩子接触的表示时，应及时给予表扬和鼓励。父母千万不要因为孩子害怕与外人接触而把他们关在家里。\r\n6、多带孩子进行滑板、秋千、平衡木等游戏。这对减轻行为障碍，增加交流等有较好效果。', 'resources/19.jpg');
INSERT INTO `introduction` VALUES (20, '自恋型人格障碍', '自恋型人格障碍（Narcissistic Personality Disorder）是一种心理障碍，表现为对自己的过度关注和自我中心。NPD者通常认为自己是特别的、卓越的，并追求持续的赞美和崇拜。他们将自己置于一切之上，常常忽视他人的需求和感受。每一个NPD，可能也在经历着其自身的痛苦和挣扎。NPD的形成，与其成长环境紧密相关。', '关于自恋性人格障碍的成因，经典精神分析理论的解释是：患者无法把自己本能的心理力量投注到外界的某一客体上，该力量滞留在内部，便形成了自恋。现代客体关系理论认为，自恋性人格障碍者的特点是“以自我为客体”，通俗地说，就是“你我不分、他我不分”。造成这种现象的原因是，患者在早年的经历中体验过人际关系上的创伤，如与父母长期分离、父母关系不和或者父母对其态度过于粗暴或过于溺爱等等。有这样一些经历，使患者觉得自己爱自己才是安全的、理所应当的。', '1. 心理疗法：如认知行为疗法和心理动力疗法，可以帮助患者识别和改变自我中心的思维模式，提高同理心和解决问题的能力。\r\n2. 药物治疗：如抗抑郁药和抗焦虑药，可以缓解患者的情绪问题和心理压力。\r\n3. 针对共病的治疗，如双相情感障碍、强迫障碍、抑郁障碍等。\r\n4. 物理治疗。\r\n5. 精神动力学心理治疗可能有效，侧重于潜在的冲突。', 'resources/20.png');
INSERT INTO `introduction` VALUES (21, 'NewType', 'NewSymptom', 'NewCauses', 'NewTreatment', 'NewPicture');
INSERT INTO `introduction` VALUES (22, 'NewType', 'NewSymptom', 'NewCauses', 'NewTreatment', 'NewPicture');
INSERT INTO `introduction` VALUES (23, 'NewType', 'NewSymptom', 'NewCauses', 'NewTreatment', 'NewPicture');
INSERT INTO `introduction` VALUES (24, 'NewType', 'NewSymptom', 'NewCauses', 'NewTreatment', 'NewPicture');
INSERT INTO `introduction` VALUES (25, 'NewType', 'NewSymptom', 'NewCauses', 'NewTreatment', 'NewPicture');

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `Mid` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员id，自增',
  `MEmail` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员邮箱，不为空',
  `MName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员名称，不为空',
  `AvatarManger` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员头像',
  `ManagerPSW` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员密码',
  PRIMARY KEY (`Mid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of manager
-- ----------------------------

-- ----------------------------
-- Table structure for quiz
-- ----------------------------
DROP TABLE IF EXISTS `quiz`;
CREATE TABLE `quiz`  (
  `Qid` int(11) NOT NULL AUTO_INCREMENT COMMENT '测试id，自增',
  `QName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '测试名称',
  `Qtag` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '测试标签',
  PRIMARY KEY (`Qid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quiz
-- ----------------------------
INSERT INTO `quiz` VALUES (1, 'NewQuizName', 'NewQuizTag');
INSERT INTO `quiz` VALUES (2, 'NewQuizName', 'UpdatedQuizTag');
INSERT INTO `quiz` VALUES (4, 'NewQuizName', 'NewQuizTag');
INSERT INTO `quiz` VALUES (5, 'NewQuizName', 'NewQuizTag');

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`  (
  `Rid` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源id，自增',
  `Rname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `RTime` datetime NULL DEFAULT NULL,
  `Rurl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Rstore` int(11) NULL DEFAULT NULL,
  `Rtag` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Rabstract` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `aimg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `Rauthor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Rid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (1, '偏执型人格障碍指南', '2022-04-01 00:00:00', 'https://www.msdmanuals.cn/home/mental-health-disorders/schizophrenia-and-related-disorders/delusional-disorder?query=%E8%A2%AB%E5%AE%B3%E5%A6%84%E6%83%B3%EF%BC%9A', 0, '被害妄想症', '偏执型人格障碍以对他人持久的怀疑和不信任为特征，患者常误解他人动机。治疗通常包括认知行为疗法和药物治疗。', 'assets/imgs/article/1.jpg', NULL);
INSERT INTO `resource` VALUES (2, '边缘型人格障碍手册', '2023-01-01 00:00:00', 'https://www.nami.org/Your-Journey/Individuals-with-Mental-Illness/Borderline-Personality-Disorder', 0, '边缘型人格障碍', '边缘型人格障碍患者常经历情绪波动和冲动行为，治疗方法包括 DBT（辩证行为疗法）。', 'assets/imgs/article/2.png', NULL);
INSERT INTO `resource` VALUES (3, '边缘型人格障碍指南', '2023-01-01 00:00:00', 'https://www.nimh.nih.gov/health/topics/borderline-personality-disorder', 0, '边缘型人格障碍', '边缘型人格障碍表现为情绪不稳定、强烈的被抛弃恐惧和冲动行为，患者常经历人际关系的剧烈波动。', 'assets/imgs/article/3.jpg', NULL);
INSERT INTO `resource` VALUES (4, '关于酒精和药物的重要事实-在美国面对成瘾', '2020-01-01 00:00:00', 'https://www.ncbi.nlm.nih.gov/books/NBK424847/', 0, '成瘾', '成瘾是一种复杂的疾病，涉及大脑的奖励系统，治疗通常包括心理治疗和药物干预。', 'assets/imgs/article/4.jpg', '美国卫生与公众服务部');
INSERT INTO `resource` VALUES (5, 'THE NEUROBIOLOGY OF SUBSTANCE USE, MISUSE, AND ADDICTION', '2023-01-01 00:00:00', 'https://www.apa.org/topics/ptsd', 0, '创伤后应激障碍', '创伤后应激障碍是对创伤经历的反应，症状包括闪回、回避和情绪麻木，治疗包括认知行为疗法和药物。', 'assets/imgs/article/5.jpg', NULL);
INSERT INTO `resource` VALUES (6, 'PTSD 症状与治疗', '2023-01-01 00:00:00', 'https://www.nimh.nih.gov/health/topics/post-traumatic-stress-disorder-ptsd', 0, '创伤后应激障碍', 'PTSD 影响许多人，尤其是经历过创伤事件的人，常见症状包括焦虑和抑郁。', 'assets/imgs/article/6.jpg', NULL);
INSERT INTO `resource` VALUES (7, '多动症治疗指南', '2023-01-01 00:00:00', 'https://www.cdc.gov/adhd/about/?CDC_AAref_Val=https://www.cdc.gov/ncbddd/adhd/facts.html', 0, '多动症', '多动症是一种常见的神经发育障碍，表现为注意力不集中和过度活跃，治疗包括行为疗法和药物。', 'assets/imgs/article/7.png', NULL);
INSERT INTO `resource` VALUES (8, '反社会人格障碍研究', '2020-01-01 00:00:00', 'https://www.ncbi.nlm.nih.gov/books/NBK207194/', 0, '反社会型人格障碍', '反社会型人格障碍的患者常表现出冲动和欺骗行为，社会功能受损。', 'assets/imgs/article/8.png', NULL);
INSERT INTO `resource` VALUES (9, 'Antisocial Personality Disorder: Often Overlooked and Untreated', '2023-01-01 00:00:00', 'https://www.psychiatry.org/News-room/APA-Blogs/Antisocial-Personality-Disorder-Often-Overlooked', 0, '反社会型人格障碍', 'ASPD 患者通常缺乏同情心，经常被忽视和未治疗，治疗难度较大，常需结合心理治疗和药物治疗。', 'assets/imgs/article/9.jpg', NULL);
INSERT INTO `resource` VALUES (10, '分裂样人格障碍解析指南', '2020-01-01 00:00:00', 'https://zhuanlan.zhihu.com/p/670363612', 0, '分裂样人格障碍', '疯子中的天才、孤僻的旁观者：分裂样人格障碍。', 'assets/imgs/article/10.jpg', NULL);
INSERT INTO `resource` VALUES (11, '分裂样人格障碍解析', '2020-01-01 00:00:00', 'https://en.wikipedia.org/wiki/Schizoid_personality_disorder', 0, '分裂样人格障碍', '是一种人格障碍，其特征是对社会关系缺乏兴趣，倾向于孤独或隐蔽的生活方式、隐瞒、情感冷淡、疏离和冷漠。受影响的个体可能无法与他人形成亲密的依恋，同时拥有一个丰富而复杂但完全内在的幻想世界。', 'assets/imgs/article/11.jpg', NULL);
INSERT INTO `resource` VALUES (12, '分裂型人格障碍症状', '2020-01-01 00:00:00', 'https://www.mayoclinic.org/diseases-conditions/schizotypal-personality-disorder/symptoms-causes/syc-20353919', 0, '分裂型人格障碍', '分裂型人格障碍患者通常被描述为古怪或古怪，他们通常很少（如果有的话）亲密关系。他们通常不知道关系是如何形成的，也不知道他们的行为如何影响他人。他们还倾向于误解他人的动机和行为，并极度不信任他人。', 'assets/imgs/article/12.png', NULL);
INSERT INTO `resource` VALUES (13, '一直在逃跑的人生：回避型人格障碍万字科普', '2020-01-01 00:00:00', 'https://zhuanlan.zhihu.com/p/666931809', 0, '回避型人格障碍', '一直在逃跑的人生：回避型人格障碍万字科普。与他们接触的人，常常觉得他们冷若冰霜、难以靠近。你的积极主动很难得到同等的回应，他们也很少向你表达真实的想法和感受。但事实上，回避型的「回避」是被动的，行为上他们逃得远远的，内心却十分想与他人发展亲密的关系，这种每时每刻的矛盾让他们备受折磨。而当这种折磨变成更为持久的、适应不良的、缺乏弹性的，就有可能发展成一种回避型人格障碍。', 'assets/imgs/article/13.jpg', NULL);
INSERT INTO `resource` VALUES (14, '成瘾疾病研究', '2020-01-01 00:00:00', 'https://www.msdmanuals.cn/home/mental-health-disorders/personality-disorders/avoidant-personality-disorder#%E7%97%87%E7%8A%B6_v36026687_zh', 0, '回避型人格障碍', '医生诊断回避型人格障碍的标准是，患者必须持续避免社交接触，有不恰当感，并对批评和拒绝过分敏感，至少符合以下中的 4 点：在工作中，他们避免涉及与人际接触的活动，因为他们担心他们会受到批评或拒绝，或者人们会反对他们。除非他们确定被人喜欢，否则他们不愿意与人打交道。他们只留在亲密关系中，因为他们害怕被嘲笑或羞辱。他们先入为主地认为在社交场合会受到批评或被拒绝。他们在新的社交场合特别拘束，因为他们觉得自己不适当。他们认为自己社交无能，没有吸引力，或比其他人逊色。他们不愿冒险或参加任何新活动，因为他们可能会感到尴尬。此外，症状早在成年早期就已经开始了。', 'assets/imgs/article/14.jpg', NULL);
INSERT INTO `resource` VALUES (15, '焦虑症治疗手册', '2020-01-01 00:00:00', 'https://www.ncbi.nlm.nih.gov/pmc/articles/PMC7071237/', 0, '焦虑', '焦虑症的种类繁多，包括广泛性焦虑症和社交焦虑障碍，治疗方法包括药物和心理治疗。', 'assets/imgs/article/15.png', NULL);
INSERT INTO `resource` VALUES (16, '进食障碍研究', '2023-01-01 00:00:00', 'https://www.nimh.nih.gov/health/topics/eating-disorders', 0, '进食障碍', '进食障碍包括神经性厌食和神经性贪食，通常与心理因素密切相关。', 'assets/imgs/article/16.jpg', 'NIMH');
INSERT INTO `resource` VALUES (17, 'Transforming the understanding and treatment of mental illnesses.', '2023-01-01 00:00:00', 'https://www.nimh.nih.gov/health/statistics/schizophrenia', 0, '精神分裂症', '精神分裂症是一种严重的精神障碍，表现为幻觉、妄想和认知功能障碍。', 'assets/imgs/article/17.jpg', NULL);
INSERT INTO `resource` VALUES (18, '恐慌症症状与治疗', '2023-01-01 00:00:00', 'https://www.nimh.nih.gov/health/statistics/panic-disorder', 0, '恐慌症', '恐慌症表现为突发的强烈恐惧，伴随身体症状，可能导致患者避免特定场合。', 'assets/imgs/article/18.jpg', NULL);
INSERT INTO `resource` VALUES (19, '强迫症治疗指南', '2024-01-01 00:00:00', 'https://www.msdmanuals.cn/home/mental-health-disorders/obsessive-compulsive-and-related-disorders/obsessive-compulsive-disorder-ocd', 0, '强迫症', '强迫症 (OCD) 患者存在强迫观念——反复出现一些想法、画面或冲动，即使患者并不想这样。这些强迫观念可侵入患者的脑海，即使是在患者考虑或做其他事情时。另外，强迫观念通常可导致极大的困苦或焦虑。强迫观念的主题包括伤害（对自己或他人）、清洗或污染、禁止或禁忌的想法（例如，攻击性或性强迫观念）以及苛求对称性。担心污染（如担心接触门把手会引起疾病）多疑（例如担心家正门没有锁）担心物品没有完全排整齐或放平由于强迫观念不使人愉悦，所以患者经常会试图忽视和／或控制它们。', 'assets/imgs/article/19.jpg', NULL);
INSERT INTO `resource` VALUES (20, '双相情感障碍手册', '2023-01-01 00:00:00', 'https://www.nimh.nih.gov/health/statistics/bipolar-disorder', 0, '双相情感障碍', '双相情感障碍表现为情绪的极端波动，包括躁狂和抑郁发作。', 'assets/imgs/article/20.jpg', NULL);
INSERT INTO `resource` VALUES (21, '睡眠相位后移综合征', '2023-01-01 00:00:00', 'https://www.nimh.nih.gov/health/statistics/bipolar-disorder', 0, '睡眠相位后移综合征 DSPS', '睡眠开始和醒来的时间，比理想情况不可控制的晚。每天入睡时间基本相同。一旦入睡，并不会轻易醒来。在早晨理想时间起床是非常困难的。相对非常严重的，没有能力去把睡眠相位提前，如强迫在正常的时间睡觉和起床。', 'assets/imgs/article/21.jpg', NULL);
INSERT INTO `resource` VALUES (22, '偷窃癖症状与治疗', '2024-01-01 00:00:00', 'https://www.mayoclinic.org/zh-hans/diseases-conditions/kleptomania/symptoms-causes/syc-20364732', 0, '偷窃癖 Kleptomania', '偷窃癖是一种心理健康障碍，表现为反复且无法抑制的偷窃冲动，并且偷窃的物品通常您实际上并不需要。偷窃的物品往往价值不大，您可以买得起。偷窃癖一种罕见却非常严重的问题。如果不加以治疗，可能对您和亲人造成情感伤害，甚至法律问题。偷窃癖是一种冲动控制障碍，表现为情感或行为自控出现问题。如果患有冲动控制障碍，您会很难抵抗诱惑或强烈冲动，做一些对自己或他人过分或有害的行为。', 'assets/imgs/article/22.jpg', NULL);
INSERT INTO `resource` VALUES (23, '抑郁症症状与治疗', '2023-03-31 00:00:00', 'https://www.who.int/zh/news-room/fact-sheets/detail/depression', 0, '抑郁症 Depression', '一个人会经历抑郁情绪（感到悲伤、烦躁、空虚）。他们可能会感到失去乐趣或对活动的兴趣。注意力不集中过度内疚或自我价值感低下对未来感到绝望有死亡或自杀的想法睡眠紊乱食欲或体重变化感觉非常疲倦或精力不足。', 'assets/imgs/article/23.jpg', 'author1');
INSERT INTO `resource` VALUES (24, '自闭症谱系障碍解析', '2023-03-31 00:00:00', 'https://www.msdmanuals.cn/home/children-s-health-issues/learning-and-developmental-disorders/autism-spectrum-disorder', 0, '自闭症 Autismn', '管患儿在和父母分开后会表现得沮丧，但他们不像其他儿童一样从父母那里寻找安全感。大龄儿童多会自己玩游戏，不会形成亲密的私人关系，尤其是在家之外的地方。与其他孩子互动时，他们可能不会使用眼神交流和面部表情来建立社交联系，并且难以解读他人的情绪和表情。他们可能很难知道如何以及何时加入对话，并且难以识别不适当或有害的言语。这些因素可能会导致其他人将其视为古怪或怪异的人，从而导致社交孤立。自闭症谱系障碍患者通常对变化的抗拒很强，例如新食物、玩具、家具布置和衣服。他们可能过度依恋特定的无生命物体。他们经常重复地做某件事。年幼或更严重的患儿常重复某些动作，如摇摆、挥手或转动物体等。有些患者可能通过重复的行为伤害自己，如撞头或咬自己。患病较轻的人可能会多次观看同一视频，或者坚持每餐都吃相同食物。患有 ASD 的人通常会有非常专业、不同寻常的兴趣。例如，孩子可能会沉迷于真空吸尘器。', 'assets/imgs/article/24.jpg', 'author2');
INSERT INTO `resource` VALUES (25, '自恋型人格障碍研究', '2023-01-01 00:00:00', 'https://www.psychologytoday.com/us/conditions/narcissistic-personality-disorder', 0, '自恋型人格障碍', '自恋型人格障碍表现为对自我的过度关注和缺乏同情心，患者常寻求他人的赞美。', 'assets/imgs/article/25.jpg', 'author3');

-- ----------------------------
-- Table structure for resource_video
-- ----------------------------
DROP TABLE IF EXISTS `resource_video`;
CREATE TABLE `resource_video`  (
  `Rid` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源id，自增',
  `Rname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `RTime` datetime NULL DEFAULT NULL,
  `Rurl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Rstore` int(11) NULL DEFAULT NULL,
  `Rtag` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Rabstract` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `vimg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Rid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 224 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of resource_video
-- ----------------------------
INSERT INTO `resource_video` VALUES (200, '【科普】被害妄想症', '2023-12-23 16:31:34', 'https://www.bilibili.com/video/BV1Vi4y1Y76a/?vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '被害妄想症', '被害妄想症是一种精神疾病，患者坚信自己被他人恶意迫害，表现为极度敏感多疑、幻觉频发及难以区分现实与幻想，多见于孤寡老人及精神受创者。', 'assets/imgs/video/1.png');
INSERT INTO `resource_video` VALUES (201, '什么是边缘型人格障碍|特征和症状', '2024-09-23 10:08:00', 'https://www.bilibili.com/video/BV1Ars2efEod?spm_id_from=333.788.videopod.sections&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '边缘型人格障碍', '介绍了边缘型人格障碍（BPD）的基本概念及其典型特征，通过形象生动的比喻描绘了BPD患者情绪波动大、自我认知不稳定的特性。引用日本电影《惹人嫌弃的松子》中的女主角作为典型案例，深入剖析了BPD患者的内心世界。同时，简要提及了多重人格障碍（DID）与BPD的关联，并预告了后续将探讨BPD症状成因的内容。', 'assets/imgs/video/2.png');
INSERT INTO `resource_video` VALUES (202, '边缘型人格障碍介于毁灭与清醒之间,常有被害妄想的恐惧焦虑【脑科学】', '2023-06-14 22:05:17', 'https://www.bilibili.com/video/BV1wh4y137Sw/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '边缘型人格障碍、焦虑、抑郁、恐慌', '探讨了边缘型人格障碍（BPD）的概念、成因及其对个体的影响。通过生动的比喻，文章描述了BPD患者内心的痛苦与挣扎，指出其根源在于人格的边缘化。', 'assets/imgs/video/3.png');
INSERT INTO `resource_video` VALUES (203, '了解成瘾的神经机制', '2023-07-24 16:13:31', 'https://www.bilibili.com/video/BV18j411R7tm/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '成瘾', '本视频介绍了肾上腺瘤引发的成瘾机制，并说明了毒品如何破坏这一系统。', 'assets/imgs/video/4.png');
INSERT INTO `resource_video` VALUES (204, '【干货】成瘾始于痛苦，戒瘾终于平衡！深度解读多巴胺机制，用身体“内稳态”禁欲、戒瘾，保持自律', '2023-08-03 18:00:00', 'https://www.bilibili.com/video/BV1Vm4y1x7JF/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '成瘾', '视频讲述了成瘾的本质和多巴胺在成瘾中的作用机制。成瘾行为虽然能带来短暂的快乐，但长期重复会导致快乐感受的丧失，并带来更深的痛苦。多巴胺，作为一种神经递质，在大脑中的作用不仅仅是带来快乐，更重要的是驱动人们产生获得奖励的动机。大脑中有自我调节机制，会通过痛苦来平衡过度的快乐。视频还介绍了三种戒瘾的方法：以痛治痛，物理阻断和激进诚实，帮助恢复大脑奖赏回路的平衡，降低戒断痛苦，提高自控力。', 'assets/imgs/video/5.png');
INSERT INTO `resource_video` VALUES (205, '【TED科普】这12个迹象表明，你可能患上了复杂型创伤后应激障碍', '2024-06-21 21:36:02', 'https://www.bilibili.com/video/BV1ff421B7VU/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '创伤后应激障碍', '视频介绍了复杂型创伤后应激障碍（ Complex PTSD）的基本概念、症状以及可能的治愈方法。这种心理状况是在长时间暴露于创伤性事件中，如情感忽视、欺凌、虐待等，尤其是在生命的前15年内经历这些事件的人。视频列举了12个 Complex PTSD的主要症状，并鼓励观众自我反思这些症状是否适用于他们。最后，视频提出了治愈复杂型PTSD的一些方法，强调了学会爱自己、寻找合适的治疗师以及重新学习如何给予和接受爱的重要性。', 'assets/imgs/video/6.png');
INSERT INTO `resource_video` VALUES (206, '创伤制造ADHD注意力障碍?多巴胺封锁会加重多动症抑郁焦虑反应', '2024-03-24 21:00:52', 'https://www.bilibili.com/video/BV1pK421e7AC/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '多动症、焦虑、抑郁', '视频讲述了注意力缺陷障碍（ADHD）对孩子的影响，以及如何通过合适的环境和教育来帮助他们。导演通过跟踪拍摄三名ADHD孩子的日常生活，揭示了他们在学习上的困难和社会对待他们的态度。视频还讨论了多巴胺水平对ADHD症状的影响，以及如何通过生活方式的改变来缓解ADHD症状。', 'assets/imgs/video/7.png');
INSERT INTO `resource_video` VALUES (207, '又妖又神？反社会人格障碍到底什么样？', '2023-08-05 19:00:00', 'https://www.bilibili.com/video/BV1iN411h7bf/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '反社会型人格障碍', '本视频澄清了反社会人格障碍（ASPD）与反社会行为和精神病态的区别，探讨了ASPD的核心特征、影响因素和治疗。ASPD患者缺少一部分情绪感知能力，无法直觉地体会内疚、焦虑、罪恶感等情绪。高压环境、易感基因和精神病史等因素增加了ASPD的风险，但不是必然的充分条件。治疗应包括良好的家庭教育、心理辅导和常识规训，以帮助ASPD患者学会用逻辑替代共情的生存方式。', 'assets/imgs/video/8.png');
INSERT INTO `resource_video` VALUES (208, '【精神分析】分裂样人格科普/人格特质/防御机制/形成原因/相处方式', '2022-01-10 23:07:40', 'https://www.bilibili.com/video/BV1ab4y1H7JE/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '分裂样人格障碍', '视频详细介绍了分裂样人格的特质、防御机制、形成原因及相处方式，强调分裂样人格并非精神分裂，而是一种独特的人格特质，表现为与世隔绝、漠视社会期待、强烈的自我保护意识和对亲密关系的矛盾情感。视频还探讨了分裂样人格的形成原因，包括先天敏感性和后天环境影响，并提供了与这类人格相处的建议。', 'assets/imgs/video/9.png');
INSERT INTO `resource_video` VALUES (209, '详解分裂型人格的11个特征【给力心理】', '2023-08-02 17:56:16', 'https://www.bilibili.com/video/BV1vP41147EK/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '分裂型人格障碍', '视频详细介绍了分裂型人格的11个特征，包括牵连观念、奇异的信念和想法、知觉障碍、逻辑混乱的思维和语言、猜疑或偏执观念、不恰当或受限制的情感、古怪的行为或外表、缺乏密友、过度的社交焦虑和社交恐惧、侵略性较强、以及高共病率。视频强调了分裂型人格者的行为和思维方式，提醒我们在早期识别这些特征后保持安全距离。', 'assets/imgs/video/10.png');
INSERT INTO `resource_video` VALUES (210, '回避型人格如何疗愈自己？这里有5条建议【武志红】', '2021-07-09 12:24:03', 'https://www.bilibili.com/video/BV1BM4y1M796/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '回避型人格障碍、焦虑', '本视频讲述了回避型人格障碍的特点、成因以及如何与之相处和自我疗愈。回避型人格障碍是一种心理疾病，其特点包括害怕批评、否定或排斥而回避涉涉及到人接触较多的职业活动，不愿意被人浇灌，害羞或怕被嘲弄而在亲密关系中表现拘谨等。视频作者强调，面对回避型人格障碍，我们需要保持自己的风格，理解并接纳他们，同时要直面真相，不要过度贬低自己，尝试控制可以控制的人或事物，并寻求专业人士的帮助。', 'assets/imgs/video/11.png');
INSERT INTO `resource_video` VALUES (211, '你不知道的7种焦虑症', '2023-08-23 20:09:33', 'https://www.bilibili.com/video/BV1cw411Q7C2?spm_id_from=333.788.videopod.sections&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '焦虑症、创伤后应激障碍、恐慌', '视频介绍了七种焦虑症：广泛性焦虑症、分离焦虑症、社交恐惧症和选择性缄默症、急性焦虑症（惊恐发作）、广场恐惧症、特定恐惧症和创伤后引起障碍、强迫症。虽然这些症状在某些情境下是正常的，但长期的焦虑和恐惧感会影响正常生活和工作。', 'assets/imgs/video/12.png');
INSERT INTO `resource_video` VALUES (212, '【心理学】6种进食障碍：暴饮暴食，神经性厌食症？', '2019-03-25 01:32:37', 'https://www.bilibili.com/video/BV1hb41147kG/?spm_id_from=333.337.search-card.all.click', 0, '进食障碍', '视频讨论了六种饮食失调类型，旨在打破刻板印象并提供知识以帮助那些患有此类失调症的人。视频分享了个人经历，并强调了寻求治疗以及不让饮食失调定义一个人生活的重要性。', 'assets/imgs/video/13.png');
INSERT INTO `resource_video` VALUES (213, '【TED科普】什么是精神分裂症（中英字幕）', '2023-06-04 19:25:50', 'https://www.bilibili.com/video/BV1XM4y1q7jt/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '精神分裂', '精神分裂症是一种由多种相关症状组成的综合症，它可能涉及许多与其类似但有不同原因的疾病。每个精神分裂症患有略有不同的症状，第一次表现可能是微妙的个性变化、恼易或不寻常的思想渐入。患有精神分裂症的人在初次心智症状发作时通常获得诊断，这通常在男性晚 teenager 或 early twenties，女性晚 twenties 或 early thirties 初发。精神分裂症的治疗包括心智症状阻止剂和其他干预，如认知行为治疗和再次刺激疗法。早期干预至关重要，长期未经治疗的精神分裂症症状可能会嵌入个性中，并且与此诊断相关的非人性化标签可能会阻止人们寻求帮助。', 'assets/imgs/video/14.png');
INSERT INTO `resource_video` VALUES (214, '惊恐发作（也称恐慌症）症状、病因和疗法', '2020-05-19 10:30:23', 'https://www.bilibili.com/video/BV1FC4y1p7rS/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '恐慌症、抑郁、创伤后应激障碍', '惊恐症是一种心理障碍，主要表现为患者突然出现强烈的恐惧或不适，伴有心悸、呼吸急促和颤抖等身体症状。这些症状可能非常可怕，有时会让患者认为自己正在心脏病发作或患有其他危及生命的疾病。', 'assets/imgs/video/15.png');
INSERT INTO `resource_video` VALUES (215, '严肃地科普一下强迫症', '2022-04-01 16:51:34', 'https://www.bilibili.com/video/BV1LY41177XP/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '强迫症、抑郁、焦虑、精神分裂', '视频介绍了强迫症的相关知识，包括强迫症的定义、临床症状、病因以及治疗方法。强迫症可以分为强迫思维性和强迫行为性，其中强迫行为包括强迫检查、强迫秩序、强迫清洗等，强迫思维包括强迫忧虑、强迫画面等。病因涉及心理、生理、社会三方面的因素。治疗强迫症主要有药物治疗和心理治疗两种方法。', 'assets/imgs/video/16.png');
INSERT INTO `resource_video` VALUES (216, '【正经医生】精神科医生科普，什么是双相情感障碍！', '2021-03-24 18:07:18', 'https://www.bilibili.com/video/BV1K54y1h7AC/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '双向情感障碍、抑郁', '深圳市康宁医院精神科医生对双向障碍的介绍。双向障碍是一种间歇性的病症,患者的情绪会交替处于两个极端,可能表现为躁狂或抑郁发作。在发作期间,患者的言语和行为可能会增多或减少,严重时可能伴有一些精神病性的症状。虽然双向障碍可以治愈,但需要注意维持治疗和监测病情的变化以防止反复发作。', 'assets/imgs/video/17.png');
INSERT INTO `resource_video` VALUES (217, '睡眠相位后移综合征：小伙伴们，你最近睡得还好吗？', '2021-03-15 10:04:22', 'https://www.bilibili.com/video/BV1DV411Y7DQ/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '睡眠相位后移综合征', '视频讨论了睡眠相位后移综合征（Delayed Sleep Phase Syndrome, DSPS），这是一种常见的睡眠障碍，表现为入睡和醒来的时间比理想情况晚，且难以调整。视频还提到了熬夜对情绪和心理健康的影响，以及如何应对这种睡眠模式。', 'assets/imgs/video/18.png');
INSERT INTO `resource_video` VALUES (218, '什么是偷窃癖', '2023-09-01 09:07:53', 'https://www.bilibili.com/video/BV1aw411S7fF/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '偷窃癖', '偷窃癖是一种在变态心理支配下的反常行为，患者会反复出现不可 resist 的偷窃冲动，并且会将偷来的东西隐藏或暗暗地送给他人。这种行为通常会在偷窃前后产生紧张和满足感，并且在儿童或少年期就可能开始。', 'assets/imgs/video/19.png');
INSERT INTO `resource_video` VALUES (219, '抑郁症，是怎么改变你大脑的？', '2022-06-19 11:33:55', 'https://www.bilibili.com/video/BV1da411s7BM/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '抑郁症', '抑郁症会带来大量的压力激素，破坏前额叶（负责理性思维的区域）中神经元之间的信号传递，从而导致思维变得迟缓、记忆力下降等问题。同时，抑郁症还会侵入海马体（记忆的驿站），造成记忆力大大下降。除此之外，抑郁症还会对情绪中心LEFT habenula产生影响，使患者更容易冲动、发脾气、失眠等。', 'assets/imgs/video/20.png');
INSERT INTO `resource_video` VALUES (220, '自闭症科普', '2024-10-21 08:23:16', 'https://www.bilibili.com/video/BV1eqyqYEEkT/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '自闭症', '本视频是关于自闭症的普及科普，阐述了自闭症的特征、原因、治疗方法和关爱方式，并强调了对这些来自星星的孩子的关注和帮助对社会至关重要。', 'assets/imgs/video/21.png');
INSERT INTO `resource_video` VALUES (221, '自闭症眼中的世界到底是怎样的？', '2021-04-01 13:55:06', 'https://www.bilibili.com/video/BV16A41157PT/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '自闭症', '自闭症患儿眼中的世界。他们可能将世界理解为一款3D游戏,难以理解我们传递的信息。自闭症患者面临着生活无法自理和家庭经济的压力,但政府和社会的关注和支持正在改善他们的状况。然而,融入社会仍然需要每个人的关爱。他们是星星的孩子,与我们共同生活在这个地球上,我们任重而道远。', 'assets/imgs/video/22.png');
INSERT INTO `resource_video` VALUES (222, '9个迹象表明，Ta或有自恋型人格障碍！', '2022-06-06 16:55:59', 'https://www.bilibili.com/video/BV1C5411Q7w3/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '自恋型人格障碍', '本视频主要介绍了自恋型人格障碍的九个迹象。自恋者常常不愿意听取他人的意见，而是在自己的想法上裹足不前。他们常常在关系中欺骗，以建立自己的自尊心。他们需要不断地被赞美，对批评反应过度。他们没有内疚或羞耻感，常常利用他人来满足自己的需求。他们会贬低他人，以证明自己比别人更好。这些迹象表明自恋者可能有心理障碍。', 'assets/imgs/video/23.png');
INSERT INTO `resource_video` VALUES (223, '如何对付NPD型人格？自恋人格障碍者是你能拯救的吗？', NULL, 'https://www.bilibili.com/video/BV1P14y117Ry/?spm_id_from=333.337.search-card.all.click&vd_source=14d775208ff520b9e2b9c6265544ea73', 0, '自恋型人格障碍', NULL, 'assets/imgs/video/24.png');

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `Tid` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `Tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Tid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES (00000000001, '被害妄想症', 'https://www.kuioo.cn/do-you-have-persecutory-delusions/question/', '测试你有被害妄想症吗？', '“出现幻觉妄想！测试你有被害妄想症吗？”是一项被害妄想症测试，通过模拟情景和行为反应，评价个体是否存在被害妄想倾向，帮助了解个人心理状态，提倡心理健康意识。', 'assets/imgs/tests/1.png');
INSERT INTO `test` VALUES (00000000002, '边缘型人格障碍', 'https://www.kuioo.cn/borderline-personality-disorder-bpd-test/question/', '「边缘型人格测试」你有BPD边缘性人格障碍吗？', '“边缘型人格测试”是一种用于评价BPD（边缘性人格障碍）特征的心理健康工具。通过bpd测试，可以更好地了解个人的心理状态，寻求专业帮助。', 'assets/imgs/tests/2.png');
INSERT INTO `test` VALUES (00000000003, '多动症', 'https://www.kuioo.cn/adult-adhd-test-do-you-have-attention-deficit-hyperactivity-disorder/question/', '成人多动症测试：你有注意力缺陷多动障碍吗？', '事实上，研究表明，在18岁至44岁的成年人中，约有4%的人患有多动症。对于一些成年人来说，他们的多动症可能从小就没有得到诊断和治疗，这进一步加剧了他们面临的挑战。如果你担心自己可能患有多动症，可以考虑参加多动症测试，看看是否有必要由合格的医疗专业人员进行进一步评估。早期诊断和适当的治疗可以帮助多动症患者更好地控制症状，提高生活质量。', 'assets/imgs/tests/3.png');
INSERT INTO `test` VALUES (00000000004, '反社会型人格障碍', 'https://www.kuioo.cn/am-i-antisocial-antisocial-personality-disorder-quiz/question/', '反社会人格倾向测试', '三分钟反社会人格测试是一项用于评价个人社交行为和互动方式的测试。这项反社会人格测试可以帮助人们了解自己在社交方面可能存在的问题，并提供相应的建议和帮助。', 'assets/imgs/tests/4.png');
INSERT INTO `test` VALUES (00000000005, '回避型人格障碍', 'https://www.kuioo.cn/avoidant-personality-disorder-test-do-i-have-avpd/question/', '「回避型人格障碍测试」你有回避性人格吗？', '“回避型人格障碍测试”是一种心理评价工具，用于评价个体是否具有回避型人格特征，如过度敏感、社交恐惧和低自尊。通过测试，可以更好地理解自己的行为模式，寻求帮助。', 'assets/imgs/tests/5.png');
INSERT INTO `test` VALUES (00000000006, '焦虑', 'https://www.kuioo.cn/anxiety-test-find-out-your-anxiety-level/question/', '焦虑症测试：你的焦虑程度有多深？', '焦虑症是一种常见的情绪障碍，它以过度、持续的担忧和恐惧为特征。这种情绪状态可能会严重影响个人的日常生活和心理健康。了解焦虑症并评价自己的焦虑水平，对于采取适当的应对措施至关重要。', 'assets/imgs/tests/6.png');
INSERT INTO `test` VALUES (00000000007, '恐慌症', 'https://www.kuioo.cn/panic-disorder-test-do-i-have-panic-disorder/question/', '「恐慌症症状测试」你的恐慌症指数有多高？', '想知道你的恐慌指数吗？试试“恐慌症测试”，简单几个问题，轻松了解你的焦虑水平，帮助你更好地管理情绪。', 'assets/imgs/tests/7.png');
INSERT INTO `test` VALUES (00000000008, '强迫症', 'https://www.kuioo.cn/obsessive-compulsive-personality-disorder-test-ocpd/question/', '「强迫症测验」你有强迫型人格障碍倾向吗？', '「强迫症测验」快速评估你的强迫症倾向，你有强迫型人格障碍倾向吗？测强迫症指数了解你是否有过度强迫的行为和思维，为你的心理健康提供一个自我了解的窗口。', 'assets/imgs/tests/8.png');
INSERT INTO `test` VALUES (00000000009, '抑郁症', 'https://www.kuioo.cn/depression-quiz-am-i-depressed/question/', '抑郁症心理测验「你有否患抑郁症？」快速测试', '探索心理健康，快速了解是否可能患有抑郁症。这个简短的自我评估抑郁症测试提供初步筛查，帮助你识别抑郁症状，及时寻求专业帮助。在我们的日常生活中，心理健康问题越来越受到关注，其中抑郁症是一种常见的情绪障碍。为了帮助个人更好地理解自己的心理状态，我们提供了一系列的抑郁症测试，抑郁症测试旨在评价您可能面临的抑郁症状，并提供相应的建议和指导。', 'assets/imgs/tests/9.png');
INSERT INTO `test` VALUES (00000000010, '自闭症', 'https://www.kuioo.cn/autism-spectrum-quotient-test-are-you-autistic/question/', '自闭症倾向测试', '如果你怀疑自己或你认识的人可能患有自闭症，从合格的医疗保健提供者那里获得专业诊断是很重要的。亚斯伯格症测试可以帮助你识别自闭症的一些常见症状，但它不能代替专业诊断。', 'assets/imgs/tests/10.png');
INSERT INTO `test` VALUES (00000000011, '自恋型人格障碍', 'https://www.kuioo.cn/narcissistic-personality-disorder-quiz/question/', '「自恋型人格测试」测你有多自恋？', '你是否有过度关注自己外貌或成就的倾向？是否经常期待别人给予赞美和关注？通过自恋型人格测试，你可以探索自己是否具有自恋型人格的特征。这个自恋型人格测试虽然不是临床诊断，但可以提供关于自我形象和行为模式的有趣见解。', 'assets/imgs/tests/11.png');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `Uid` int(10) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '用户的id，10位数字，唯一值，自增',
  `Email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱，不为空',
  `UName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'HelloWorld' COMMENT '用户名，不为空，默认值：HelloWorld',
  `AvatarUser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户自定义头像，图片相对路径',
  `UserPSW` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  PRIMARY KEY (`Uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (0000000001, 'example@example.com', 'Updated User Name', 'http://localhost:8082/uploads/1-1735194508910.png', '111111a@');
INSERT INTO `user` VALUES (0000000003, '2900399358@qq.com', 'user_1735194810791', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.yp-D-KHI3e2nN4eMBJcEVAAAAA?rs=1&pid=ImgDetMain', '123456');
INSERT INTO `user` VALUES (0000000004, 'lianglx26@mail2.sysu.edu.cn', 'user_1735195061657', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.yp-D-KHI3e2nN4eMBJcEVAAAAA?rs=1&pid=ImgDetMain', '123456');
INSERT INTO `user` VALUES (0000000005, 'test@example.com', 'New User', 'default_avatar.jpg', NULL);
INSERT INTO `user` VALUES (0000000006, 'test@example.com', 'New User', 'default_avatar.jpg', NULL);
INSERT INTO `user` VALUES (0000000007, 'test@example.com', 'New User', 'default_avatar.jpg', NULL);
INSERT INTO `user` VALUES (0000000008, 'test@example.com', 'New User', 'default_avatar.jpg', NULL);
INSERT INTO `user` VALUES (0000000009, 'test@example.com', 'New User', 'default_avatar.jpg', NULL);

SET FOREIGN_KEY_CHECKS = 1;
