-- Script để cập nhật role_id cho người dùng hiện có
-- Chạy các câu lệnh này nếu người dùng có role_id = NULL

-- Cập nhật tất cả người dùng thành khách hàng (role_id = 2)
UPDATE nguoi_dung SET role_id = 2 WHERE role_id IS NULL;

-- Hoặc cập nhật từng người dùng cụ thể
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND001';
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND002';
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND003';
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND004';
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND005';
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND006';
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND007';
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND008';
-- UPDATE nguoi_dung SET role_id = 2 WHERE ma_nguoi_dung = 'ND009';
-- UPDATE nguoi_dung SET role_id = 1 WHERE ma_nguoi_dung = 'ND010'; -- Admin

-- Lưu ý: 
-- role_id = 1: admin
-- role_id = 2: khách_hang

